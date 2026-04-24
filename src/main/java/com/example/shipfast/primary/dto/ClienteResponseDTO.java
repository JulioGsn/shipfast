package com.example.shipfast.primary.dto;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpfCnpj
) {
}
