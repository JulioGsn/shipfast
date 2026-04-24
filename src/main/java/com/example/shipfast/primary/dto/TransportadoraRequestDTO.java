package com.example.shipfast.primary.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransportadoraRequestDTO(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotBlank(message = "beanCalculadora é obrigatório")
        String beanCalculadora,

        @NotNull @DecimalMin(value = "0.1", message = "peso máximo deve ser maior que 0.1kg")
        Double pesoMaximoKg,

        @NotNull @Min(value = 1, message = "dimensão máxima deve ser >= 1cm")
        Integer dimensaoMaximaCm,

        @NotNull @Min(value = 1, message = "soma das dimensões máxima deve ser >= 1cm")
        Integer somaDimensoesMaximaCm,

        @NotNull @DecimalMin(value = "0.0", message = "preço base não pode ser negativo")
        Double precoBase,

        @NotNull @DecimalMin(value = "0.0", message = "preço por kg não pode ser negativo")
        Double precoPorKg
) {
}
