package com.example.shipfast.primary.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EtiquetaRequestDTO(
        @NotBlank(message = "cepOrigem é obrigatório")
        String cepOrigem,

        @NotBlank(message = "cepDestino é obrigatório")
        String cepDestino,

        @NotBlank(message = "nomeDestinatario é obrigatório")
        String nomeDestinatario,

        @NotNull(message = "clienteId é obrigatório")
        Long clienteId,

        @NotNull(message = "transportadoraId é obrigatório")
        Long transportadoraId,

        @NotEmpty(message = "pacotes não pode ser vazio")
        List<@Valid PacoteRequestDTO> pacotes
) {
}
