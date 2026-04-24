package com.example.shipfast.primary.dto;

import com.example.shipfast.primary.model.CategoriaPacote;

public record PacoteResponseDTO(
        Long id,
        String descricao,
        CategoriaPacote categoria,
        double valorDeclarado,
        double pesoKg,
        int alturaCm,
        int larguraCm,
        int comprimentoCm
) {
}
