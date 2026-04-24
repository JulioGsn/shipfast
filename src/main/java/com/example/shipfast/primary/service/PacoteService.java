package com.example.shipfast.primary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shipfast.audit.model.TipoOperacao;
import com.example.shipfast.audit.service.AuditoriaService;
import com.example.shipfast.exception.RecursoNaoEncontradoException;
import com.example.shipfast.primary.dto.PacoteRequestDTO;
import com.example.shipfast.primary.dto.PacoteResponseDTO;
import com.example.shipfast.primary.model.Pacote;
import com.example.shipfast.primary.repository.PacoteRepository;

@Service
public class PacoteService {

    private final PacoteRepository repository;
    private final AuditoriaService auditoria;

    public PacoteService(PacoteRepository repository, AuditoriaService auditoria) {
        this.repository = repository;
        this.auditoria = auditoria;
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<PacoteResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public PacoteResponseDTO buscar(Long id) {
        return toResponse(buscarEntity(id));
    }

    @Transactional("primaryTransactionManager")
    public PacoteResponseDTO criar(PacoteRequestDTO dto) {
        Pacote p = new Pacote();
        aplicar(p, dto);
        Pacote salvo = repository.save(p);
        auditoria.registrar("Pacote", salvo.getId(), TipoOperacao.CRIADO,
                "pacote criado: " + salvo.getDescricao());
        return toResponse(salvo);
    }

    @Transactional("primaryTransactionManager")
    public PacoteResponseDTO atualizar(Long id, PacoteRequestDTO dto) {
        Pacote p = buscarEntity(id);
        aplicar(p, dto);
        Pacote salvo = repository.save(p);
        auditoria.registrar("Pacote", salvo.getId(), TipoOperacao.ATUALIZADO,
                "pacote atualizado: " + salvo.getDescricao());
        return toResponse(salvo);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Pacote p = buscarEntity(id);
        repository.delete(p);
        auditoria.registrar("Pacote", id, TipoOperacao.REMOVIDO,
                "pacote removido: " + p.getDescricao());
    }

    Pacote buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pacote com id " + id + " não encontrado"));
    }

    private void aplicar(Pacote p, PacoteRequestDTO dto) {
        p.setDescricao(dto.descricao());
        p.setCategoria(dto.categoria());
        p.setValorDeclarado(dto.valorDeclarado());
        p.setPesoKg(dto.pesoKg());
        p.setAlturaCm(dto.alturaCm());
        p.setLarguraCm(dto.larguraCm());
        p.setComprimentoCm(dto.comprimentoCm());
    }

    private PacoteResponseDTO toResponse(Pacote p) {
        return new PacoteResponseDTO(
                p.getId(),
                p.getDescricao(),
                p.getCategoria(),
                p.getValorDeclarado(),
                p.getPesoKg(),
                p.getAlturaCm(),
                p.getLarguraCm(),
                p.getComprimentoCm()
        );
    }
}
