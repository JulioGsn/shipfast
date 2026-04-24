# ShipFast — Atividade 02 (Persistência Híbrida)

API de gestão de etiquetas de envio (domínio *shipping*, inspirado em serviços como Frenet) desenvolvida em **Spring Boot 4.0.5 + Java 17**, com persistência híbrida em **dois bancos H2 em memória**: um para os dados principais do domínio e outro exclusivo para logs de auditoria.

PPGTI 1004 — Desenvolvimento Web II — **Atividade 02**.

---

## Domínio

O domínio é a criação e o acompanhamento de **etiquetas de envio**: um cliente solicita o envio de um ou mais pacotes a um destinatário, escolhe uma transportadora, e o sistema calcula o frete com base no peso total e tabela de preços da transportadora. Cada etiqueta fica rastreável por status (`CRIADA`, `POSTADA`, `ENTREGUE`, `CANCELADA`) e regras condicionais impedem edição/remoção depois da postagem.

Ver `JUSTIFICATIVA.md` para a escolha do domínio em detalhe.

## Relacionamentos

```
          1 ┌─────────────┐ N
   ┌────────┤   Cliente   ├────────┐
   │        └─────────────┘        │
   │                               │  N (cascade ALL + orphanRemoval)
   │                               ▼
   │                        ┌─────────────┐ N             N ┌───────────┐
   │                        │   Etiqueta  ├────── etiqueta_pacote ──────┤  Pacote   │
   │                        └──────┬──────┘  (cascade PERSIST)          └───────────┘
   │                               │ N
   │                               │
   │                        ┌──────▼──────┐
   └───────────────────────►│Transportadora│
                            └─────────────┘
```

- **Cliente → Etiqueta**: `@OneToMany(cascade = ALL, orphanRemoval = true)` — ao remover um cliente, suas etiquetas são removidas junto.
- **Etiqueta → Cliente**: `@ManyToOne(fetch = LAZY)` — lado dono da FK `cliente_id`.
- **Etiqueta → Transportadora**: `@ManyToOne(fetch = LAZY)` — FK `transportadora_id`.
- **Etiqueta ↔ Pacote**: `@ManyToMany(cascade = PERSIST)` com join table `etiqueta_pacote` — um pacote pode aparecer em várias etiquetas (envios recorrentes); pacotes novos enviados no corpo da etiqueta são persistidos automaticamente.

## Arquitetura em camadas

```
com.example.shipfast
├── config/                          # 2 @Configuration — um por datasource
│   ├── PrimaryDataSourceConfig.java
│   └── AuditDataSourceConfig.java
├── exception/                       # exceções de domínio + @RestControllerAdvice
├── primary/                         # Base A — dados principais
│   ├── controller/                  # CRUD REST
│   ├── service/                     # regras de negócio + orquestração
│   ├── repository/                  # JpaRepository + queries customizadas
│   ├── model/                       # entidades JPA
│   └── dto/                         # records para entrada/saída
└── audit/                           # Base B — logs de auditoria
    ├── controller/
    ├── service/
    ├── repository/
    ├── model/
    └── dto/
```

## Persistência híbrida

Os dois datasources são configurados manualmente (auto-config do Spring Boot só cuida de um):

| Base | URL | Entidades | TxManager |
|---|---|---|---|
| **A (primary)** | `jdbc:h2:mem:primarydb` | `Cliente`, `Transportadora`, `Pacote`, `Etiqueta` | `primaryTransactionManager` (`@Primary`) |
| **B (audit)** | `jdbc:h2:mem:auditdb` | `LogAuditoria` | `auditTransactionManager` |

A cada criação/atualização/remoção na Base A, o service chama `AuditoriaService.registrar(...)`, método anotado com `@Transactional("auditTransactionManager")` — o Spring comuta para o TX manager nomeado, grava o log na Base B em transação independente, e retorna.

## Queries customizadas

| Tipo | Onde | O que faz |
|---|---|---|
| **JPQL `@Query`** | `EtiquetaRepository.buscarPorStatus` | filtra etiquetas por `StatusEtiqueta` |
| **JPQL + `JOIN FETCH`** | `EtiquetaRepository.buscarPorIdComDetalhes` | carrega etiqueta + cliente + transportadora + pacotes em uma query (evita N+1, controla o LAZY) |
| **SQL nativo** | `TransportadoraRepository.rankingUso` | ranking de transportadoras por volume de etiquetas, usando interface projection `TransportadoraRanking` |

## Endpoints REST

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/clientes` | criar cliente (`@Valid`) |
| GET | `/api/clientes` | listar |
| GET | `/api/clientes/{id}` | buscar (404 se não existe) |
| PUT | `/api/clientes/{id}` | atualizar |
| DELETE | `/api/clientes/{id}` | remover (cascade ALL → etiquetas junto) |
| POST | `/api/transportadoras` | criar transportadora |
| GET | `/api/transportadoras` | listar |
| GET | `/api/transportadoras/{id}` | buscar |
| GET | `/api/transportadoras/ranking` | **ranking via SQL nativo** |
| PUT | `/api/transportadoras/{id}` | atualizar |
| DELETE | `/api/transportadoras/{id}` | remover |
| POST | `/api/pacotes` | criar pacote avulso |
| GET | `/api/pacotes` | listar |
| GET | `/api/pacotes/{id}` | buscar |
| PUT | `/api/pacotes/{id}` | atualizar |
| DELETE | `/api/pacotes/{id}` | remover |
| POST | `/api/etiquetas` | criar etiqueta (cria pacotes junto via cascade PERSIST) |
| GET | `/api/etiquetas` | listar |
| GET | `/api/etiquetas?status=CRIADA` | **filtrar via JPQL** |
| GET | `/api/etiquetas/{id}` | buscar (usa **JOIN FETCH**) |
| PUT | `/api/etiquetas/{id}` | atualizar (bloqueado se POSTADA/ENTREGUE) |
| DELETE | `/api/etiquetas/{id}` | remover (bloqueado se POSTADA/ENTREGUE) |
| GET | `/api/auditoria` | listar logs da Base B |

## Validação e tratamento de erros

- DTOs de entrada são `record` com anotações `jakarta.validation.constraints.*` (`@NotBlank`, `@NotNull`, `@Min`, `@DecimalMin`, `@Email`, `@NotEmpty`, `@Valid` em listas).
- Controllers usam `@Valid @RequestBody`.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza:
  - `MethodArgumentNotValidException` → 400 com detalhes por campo
  - `RegraNegocioException` → 400 com mensagem da regra
  - `RecursoNaoEncontradoException` → 404

## Como rodar

Pré-requisitos: Java 17+.

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. O console H2 fica em `http://localhost:8080/h2-console` (use `jdbc:h2:mem:primarydb` ou `jdbc:h2:mem:auditdb`, usuário `sa`, senha vazia).

## Testando a API

O arquivo `postman_collection.json` contém uma coleção pronta com fluxos sequenciais. Fluxo recomendado:

1. Criar uma **transportadora** (`POST /api/transportadoras`)
2. Criar um **cliente** (`POST /api/clientes`)
3. Criar uma **etiqueta** referenciando cliente + transportadora + pacotes (`POST /api/etiquetas`) — pacotes são persistidos automaticamente via cascade PERSIST
4. Buscar a etiqueta (`GET /api/etiquetas/{id}`) — resposta vem completa graças ao JOIN FETCH
5. Listar a auditoria (`GET /api/auditoria`) — confirma os 3 registros na Base B
6. Tentar criar etiqueta com peso excessivo para ver a **regra de negócio condicional** em ação

## Stack

- Spring Boot 4.0.5, Spring Data JPA, Hibernate ORM 7.2
- H2 Database (em memória, duas instâncias)
- Jakarta Persistence 3.2, Jakarta Bean Validation 3.1
- Java 17 (records, text blocks)
