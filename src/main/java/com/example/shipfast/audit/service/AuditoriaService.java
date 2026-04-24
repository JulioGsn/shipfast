package com.example.shipfast.audit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shipfast.audit.dto.LogAuditoriaResponseDTO;
import com.example.shipfast.audit.model.LogAuditoria;
import com.example.shipfast.audit.model.TipoOperacao;
import com.example.shipfast.audit.repository.LogAuditoriaRepository;

@Service
public class AuditoriaService {

    private final LogAuditoriaRepository repository;

    public AuditoriaService(LogAuditoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional("auditTransactionManager")
    public void registrar(String entidade, Long entidadeId, TipoOperacao operacao, String detalhes) {
        LogAuditoria log = new LogAuditoria(entidade, entidadeId, operacao, detalhes);
        repository.save(log);
    }

    @Transactional(value = "auditTransactionManager", readOnly = true)
    public List<LogAuditoriaResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    private LogAuditoriaResponseDTO toResponse(LogAuditoria log) {
        return new LogAuditoriaResponseDTO(
                log.getId(),
                log.getEntidade(),
                log.getEntidadeId(),
                log.getOperacao(),
                log.getDataHora(),
                log.getDetalhes()
        );
    }
}
