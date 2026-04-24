package com.example.shipfast.primary.dto;

public record TransportadoraResponseDTO(
        Long id,
        String nome,
        String beanCalculadora,
        double pesoMaximoKg,
        int dimensaoMaximaCm,
        int somaDimensoesMaximaCm,
        double precoBase,
        double precoPorKg
) {
}
