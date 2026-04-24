package com.example.shipfast.primary.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shipfast.primary.model.StatusEtiqueta;

public record EtiquetaResponseDTO(
        Long id,
        String codigoRastreio,
        String cepOrigem,
        String cepDestino,
        String nomeDestinatario,
        StatusEtiqueta status,
        double valorFrete,
        LocalDateTime criadaEm,
        Long clienteId,
        String clienteNome,
        Long transportadoraId,
        String transportadoraNome,
        List<PacoteResponseDTO> pacotes
) {
}
