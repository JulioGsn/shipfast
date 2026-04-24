package com.example.shipfast.primary.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shipfast.audit.model.TipoOperacao;
import com.example.shipfast.audit.service.AuditoriaService;
import com.example.shipfast.exception.RecursoNaoEncontradoException;
import com.example.shipfast.exception.RegraNegocioException;
import com.example.shipfast.primary.dto.EtiquetaRequestDTO;
import com.example.shipfast.primary.dto.EtiquetaResponseDTO;
import com.example.shipfast.primary.dto.PacoteRequestDTO;
import com.example.shipfast.primary.dto.PacoteResponseDTO;
import com.example.shipfast.primary.model.Cliente;
import com.example.shipfast.primary.model.Etiqueta;
import com.example.shipfast.primary.model.Pacote;
import com.example.shipfast.primary.model.StatusEtiqueta;
import com.example.shipfast.primary.model.Transportadora;
import com.example.shipfast.primary.repository.ClienteRepository;
import com.example.shipfast.primary.repository.EtiquetaRepository;
import com.example.shipfast.primary.repository.TransportadoraRepository;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final ClienteRepository clienteRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final AuditoriaService auditoria;

    public EtiquetaService(EtiquetaRepository etiquetaRepository,
                           ClienteRepository clienteRepository,
                           TransportadoraRepository transportadoraRepository,
                           AuditoriaService auditoria) {
        this.etiquetaRepository = etiquetaRepository;
        this.clienteRepository = clienteRepository;
        this.transportadoraRepository = transportadoraRepository;
        this.auditoria = auditoria;
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<EtiquetaResponseDTO> listar() {
        return etiquetaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public EtiquetaResponseDTO buscar(Long id) {
        Etiqueta e = etiquetaRepository.buscarPorIdComDetalhes(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Etiqueta com id " + id + " não encontrada"));
        return toResponse(e);
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<EtiquetaResponseDTO> buscarPorStatus(StatusEtiqueta status) {
        return etiquetaRepository.buscarPorStatus(status).stream().map(this::toResponse).toList();
    }

    @Transactional("primaryTransactionManager")
    public EtiquetaResponseDTO criar(EtiquetaRequestDTO dto) {
        Cliente cliente = buscarCliente(dto.clienteId());
        Transportadora transp = buscarTransportadora(dto.transportadoraId());

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setCepOrigem(dto.cepOrigem());
        etiqueta.setCepDestino(dto.cepDestino());
        etiqueta.setNomeDestinatario(dto.nomeDestinatario());
        etiqueta.setCliente(cliente);
        etiqueta.setTransportadora(transp);

        Set<Pacote> pacotes = dto.pacotes().stream()
                .map(this::novoPacote)
                .collect(Collectors.toSet());
        etiqueta.setPacotes(pacotes);

        validarCompatibilidade(transp, pacotes);

        double pesoTotal = pacotes.stream().mapToDouble(Pacote::getPesoKg).sum();
        etiqueta.setValorFrete(calcularFrete(transp, pesoTotal));
        etiqueta.setCodigoRastreio(gerarCodigoRastreio(transp));
        etiqueta.setStatus(StatusEtiqueta.CRIADA);

        Etiqueta salva = etiquetaRepository.save(etiqueta);
        auditoria.registrar("Etiqueta", salva.getId(), TipoOperacao.CRIADO,
                "etiqueta criada: " + salva.getCodigoRastreio());
        return toResponse(salva);
    }

    @Transactional("primaryTransactionManager")
    public EtiquetaResponseDTO atualizar(Long id, EtiquetaRequestDTO dto) {
        Etiqueta existente = etiquetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Etiqueta com id " + id + " não encontrada"));

        if (existente.getStatus() == StatusEtiqueta.POSTADA
                || existente.getStatus() == StatusEtiqueta.ENTREGUE) {
            throw new RegraNegocioException(
                    "Etiqueta já postada ou entregue não pode ser alterada");
        }

        Cliente cliente = buscarCliente(dto.clienteId());
        Transportadora transp = buscarTransportadora(dto.transportadoraId());

        existente.setCepOrigem(dto.cepOrigem());
        existente.setCepDestino(dto.cepDestino());
        existente.setNomeDestinatario(dto.nomeDestinatario());
        existente.setCliente(cliente);
        existente.setTransportadora(transp);

        existente.getPacotes().clear();
        Set<Pacote> novosPacotes = dto.pacotes().stream()
                .map(this::novoPacote)
                .collect(Collectors.toSet());
        existente.getPacotes().addAll(novosPacotes);

        validarCompatibilidade(transp, novosPacotes);

        double pesoTotal = novosPacotes.stream().mapToDouble(Pacote::getPesoKg).sum();
        existente.setValorFrete(calcularFrete(transp, pesoTotal));

        Etiqueta salva = etiquetaRepository.save(existente);
        auditoria.registrar("Etiqueta", salva.getId(), TipoOperacao.ATUALIZADO,
                "etiqueta atualizada: " + salva.getCodigoRastreio());
        return toResponse(salva);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Etiqueta existente = etiquetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Etiqueta com id " + id + " não encontrada"));

        if (existente.getStatus() == StatusEtiqueta.POSTADA
                || existente.getStatus() == StatusEtiqueta.ENTREGUE) {
            throw new RegraNegocioException(
                    "Etiqueta já postada ou entregue não pode ser excluída");
        }

        etiquetaRepository.delete(existente);
        auditoria.registrar("Etiqueta", id, TipoOperacao.REMOVIDO,
                "etiqueta removida: " + existente.getCodigoRastreio());
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente com id " + id + " não encontrado"));
    }

    private Transportadora buscarTransportadora(Long id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Transportadora com id " + id + " não encontrada"));
    }

    private Pacote novoPacote(PacoteRequestDTO dto) {
        Pacote p = new Pacote();
        p.setDescricao(dto.descricao());
        p.setCategoria(dto.categoria());
        p.setValorDeclarado(dto.valorDeclarado());
        p.setPesoKg(dto.pesoKg());
        p.setAlturaCm(dto.alturaCm());
        p.setLarguraCm(dto.larguraCm());
        p.setComprimentoCm(dto.comprimentoCm());
        return p;
    }

    private void validarCompatibilidade(Transportadora transp, Set<Pacote> pacotes) {
        double pesoTotal = pacotes.stream().mapToDouble(Pacote::getPesoKg).sum();
        if (pesoTotal > transp.getPesoMaximoKg()) {
            throw new RegraNegocioException(
                    "Peso total " + pesoTotal + "kg excede o limite de "
                            + transp.getNome() + " (" + transp.getPesoMaximoKg() + "kg)");
        }
        for (Pacote p : pacotes) {
            int maiorDim = Math.max(Math.max(p.getAlturaCm(), p.getLarguraCm()), p.getComprimentoCm());
            if (maiorDim > transp.getDimensaoMaximaCm()) {
                throw new RegraNegocioException(
                        "Pacote '" + p.getDescricao() + "' excede a dimensão máxima de "
                                + transp.getNome() + " (" + transp.getDimensaoMaximaCm() + "cm)");
            }
            if (p.somaDimensoes() > transp.getSomaDimensoesMaximaCm()) {
                throw new RegraNegocioException(
                        "Pacote '" + p.getDescricao() + "' excede a soma de dimensões máxima de "
                                + transp.getNome() + " (" + transp.getSomaDimensoesMaximaCm() + "cm)");
            }
        }
    }

    private double calcularFrete(Transportadora transp, double pesoTotalKg) {
        return transp.getPrecoBase() + transp.getPrecoPorKg() * pesoTotalKg;
    }

    private String gerarCodigoRastreio(Transportadora transp) {
        String nome = transp.getNome();
        String prefixo = nome.toUpperCase().substring(0, Math.min(3, nome.length()));
        return prefixo + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private EtiquetaResponseDTO toResponse(Etiqueta e) {
        List<PacoteResponseDTO> pacotes = e.getPacotes().stream()
                .map(p -> new PacoteResponseDTO(
                        p.getId(),
                        p.getDescricao(),
                        p.getCategoria(),
                        p.getValorDeclarado(),
                        p.getPesoKg(),
                        p.getAlturaCm(),
                        p.getLarguraCm(),
                        p.getComprimentoCm()))
                .toList();
        return new EtiquetaResponseDTO(
                e.getId(),
                e.getCodigoRastreio(),
                e.getCepOrigem(),
                e.getCepDestino(),
                e.getNomeDestinatario(),
                e.getStatus(),
                e.getValorFrete(),
                e.getCriadaEm(),
                e.getCliente().getId(),
                e.getCliente().getNome(),
                e.getTransportadora().getId(),
                e.getTransportadora().getNome(),
                pacotes
        );
    }
}
