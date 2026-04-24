package com.example.shipfast.primary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shipfast.audit.model.TipoOperacao;
import com.example.shipfast.audit.service.AuditoriaService;
import com.example.shipfast.exception.RecursoNaoEncontradoException;
import com.example.shipfast.primary.dto.TransportadoraRequestDTO;
import com.example.shipfast.primary.dto.TransportadoraResponseDTO;
import com.example.shipfast.primary.model.Transportadora;
import com.example.shipfast.primary.repository.TransportadoraRanking;
import com.example.shipfast.primary.repository.TransportadoraRepository;

@Service
public class TransportadoraService {

    private final TransportadoraRepository repository;
    private final AuditoriaService auditoria;

    public TransportadoraService(TransportadoraRepository repository, AuditoriaService auditoria) {
        this.repository = repository;
        this.auditoria = auditoria;
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<TransportadoraResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public TransportadoraResponseDTO buscar(Long id) {
        return toResponse(buscarEntity(id));
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<TransportadoraRanking> ranking() {
        return repository.rankingUso();
    }

    @Transactional("primaryTransactionManager")
    public TransportadoraResponseDTO criar(TransportadoraRequestDTO dto) {
        Transportadora t = new Transportadora();
        aplicar(t, dto);
        Transportadora salva = repository.save(t);
        auditoria.registrar("Transportadora", salva.getId(), TipoOperacao.CRIADO,
                "transportadora criada: " + salva.getNome());
        return toResponse(salva);
    }

    @Transactional("primaryTransactionManager")
    public TransportadoraResponseDTO atualizar(Long id, TransportadoraRequestDTO dto) {
        Transportadora t = buscarEntity(id);
        aplicar(t, dto);
        Transportadora salva = repository.save(t);
        auditoria.registrar("Transportadora", salva.getId(), TipoOperacao.ATUALIZADO,
                "transportadora atualizada: " + salva.getNome());
        return toResponse(salva);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Transportadora t = buscarEntity(id);
        repository.delete(t);
        auditoria.registrar("Transportadora", id, TipoOperacao.REMOVIDO,
                "transportadora removida: " + t.getNome());
    }

    Transportadora buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Transportadora com id " + id + " não encontrada"));
    }

    private void aplicar(Transportadora t, TransportadoraRequestDTO dto) {
        t.setNome(dto.nome());
        t.setBeanCalculadora(dto.beanCalculadora());
        t.setPesoMaximoKg(dto.pesoMaximoKg());
        t.setDimensaoMaximaCm(dto.dimensaoMaximaCm());
        t.setSomaDimensoesMaximaCm(dto.somaDimensoesMaximaCm());
        t.setPrecoBase(dto.precoBase());
        t.setPrecoPorKg(dto.precoPorKg());
    }

    private TransportadoraResponseDTO toResponse(Transportadora t) {
        return new TransportadoraResponseDTO(
                t.getId(),
                t.getNome(),
                t.getBeanCalculadora(),
                t.getPesoMaximoKg(),
                t.getDimensaoMaximaCm(),
                t.getSomaDimensoesMaximaCm(),
                t.getPrecoBase(),
                t.getPrecoPorKg()
        );
    }
}
