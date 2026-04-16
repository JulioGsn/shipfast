package com.example.shipfast.service;

import com.example.shipfast.dto.CalculoFreteResponseDTO;
import com.example.shipfast.model.Etiqueta;

public interface CalculadoraFreteService {

    void validarLimites(Etiqueta etiqueta);

    CalculoFreteResponseDTO calcular(Etiqueta etiqueta);
}
