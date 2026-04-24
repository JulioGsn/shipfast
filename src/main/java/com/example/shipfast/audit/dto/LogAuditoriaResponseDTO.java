package com.example.shipfast.audit.dto;

import java.time.LocalDateTime;

import com.example.shipfast.audit.model.TipoOperacao;

public record LogAuditoriaResponseDTO(
        Long id,
        String entidade,
        Long entidadeId,
        TipoOperacao operacao,
        LocalDateTime dataHora,
        String detalhes
) {
}
