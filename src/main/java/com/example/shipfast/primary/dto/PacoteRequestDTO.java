package com.example.shipfast.primary.dto;

import com.example.shipfast.primary.model.CategoriaPacote;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PacoteRequestDTO(
        @NotBlank(message = "descrição é obrigatória")
        String descricao,

        @NotNull(message = "categoria é obrigatória")
        CategoriaPacote categoria,

        @NotNull @DecimalMin(value = "0.0", message = "valor declarado não pode ser negativo")
        Double valorDeclarado,

        @NotNull @DecimalMin(value = "0.01", message = "peso deve ser maior que 0")
        Double pesoKg,

        @NotNull @Min(value = 1, message = "altura deve ser >= 1cm")
        Integer alturaCm,

        @NotNull @Min(value = 1, message = "largura deve ser >= 1cm")
        Integer larguraCm,

        @NotNull @Min(value = 1, message = "comprimento deve ser >= 1cm")
        Integer comprimentoCm
) {
}
