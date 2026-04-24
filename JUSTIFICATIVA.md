# Justificativa da Escolha do Domínio

O domínio escolhido foi **logística de envio (shipping)**, inspirado em serviços como a Frenet. A aplicação expõe uma API para gerenciar etiquetas de envio, clientes, transportadoras e pacotes, calculando o frete com base nas regras de cada transportadora.

## Por que este domínio atende aos requisitos da Atividade 02

**Relacionamentos exigidos pelo PDF caem naturalmente no domínio:**

- **One-to-Many / Many-to-One** — um cliente possui várias etiquetas (`Cliente 1:N Etiqueta`); uma etiqueta é atendida por exatamente uma transportadora (`Etiqueta N:1 Transportadora`). Ambos os lados fazem sentido no mundo real, sem forçar a modelagem.
- **Many-to-Many** — uma etiqueta agrupa vários pacotes, e um mesmo pacote pode aparecer em várias etiquetas ao longo do tempo (envios recorrentes do mesmo item, reenvio por extravio, etc.). A tabela de junção `etiqueta_pacote` representa essa relação.
- **Cascade** — ao remover um cliente, faz sentido remover suas etiquetas em cascata (`CascadeType.ALL` + `orphanRemoval`); ao criar uma etiqueta com pacotes novos no corpo da request, faz sentido persistir os pacotes junto (`CascadeType.PERSIST`).

**Base B (auditoria) encaixa na narrativa do domínio:** logística movimenta valor e precisa de rastreabilidade. Um log imutável de quem criou, quando e em que ordem as etiquetas/clientes/transportadoras foram manipulados é um requisito de negócio plausível, não um elemento artificial inserido apenas para atender ao PDF.

**Regra de negócio condicional** — o ciclo de vida da etiqueta (`CRIADA → POSTADA → ENTREGUE | CANCELADA`) dá origem a uma regra natural: etiquetas já postadas ou entregues não podem ser alteradas nem excluídas. Além disso, cada transportadora tem limites físicos (peso máximo, dimensões máximas) que devem ser validados antes de aceitar uma etiqueta — `validarCompatibilidade` no `EtiquetaService`.

**Queries customizadas se justificam pelo uso:**
- filtrar etiquetas por status é a consulta operacional mais comum (JPQL);
- ranking de transportadoras por volume é insight de negócio clássico (SQL nativo com agregação);
- buscar o detalhe completo de uma etiqueta (com cliente, transportadora e pacotes) é o cenário perfeito para `JOIN FETCH`, porque a tela de detalhe precisa de tudo de uma vez e sem os joins seria um N+1 evidente.

Em resumo, o domínio de shipping cobre todos os requisitos da atividade sem que nenhuma parte do modelo pareça enxertada — cada relacionamento, regra e query tem razão de existir no negócio.
