package com.example.shipfast.service.impl;

import org.springframework.stereotype.Service;

import com.example.shipfast.dto.CalculoFreteResponseDTO;
import com.example.shipfast.exception.RegraNegocioException;
import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.Transportadora;
import com.example.shipfast.service.CalculadoraFreteService;

@Service("correios")
public class CorreiosCalculadoraService implements CalculadoraFreteService {

    private static final double FATOR_CUBAGEM = 6000.0;

    @Override
    public void validarLimites(Etiqueta etiqueta) {
        Transportadora t = etiqueta.getTransportadora();

        if (etiqueta.getPesoKg() > t.getPesoMaximoKg()) {
            throw new RegraNegocioException(
                    "Correios (" + t.name() + ") não aceita pacote acima de "
                            + t.getPesoMaximoKg() + " kg");
        }

        int maiorDimensao = Math.max(
                etiqueta.getAlturaCm(),
                Math.max(etiqueta.getLarguraCm(), etiqueta.getComprimentoCm()));

        if (maiorDimensao > t.getDimensaoMaximaCm()) {
            throw new RegraNegocioException(
                    "Correios (" + t.name() + ") aceita no máximo "
                            + t.getDimensaoMaximaCm() + " cm em qualquer lado");
        }

        if (etiqueta.somaDimensoes() > t.getSomaDimensoesMaximaCm()) {
            throw new RegraNegocioException(
                    "Correios (" + t.name() + ") exige soma de dimensões até "
                            + t.getSomaDimensoesMaximaCm() + " cm");
        }
    }

    @Override
    public CalculoFreteResponseDTO calcular(Etiqueta etiqueta) {
        validarLimites(etiqueta);

        double pesoCubado = (etiqueta.getAlturaCm()
                * etiqueta.getLarguraCm()
                * etiqueta.getComprimentoCm()) / FATOR_CUBAGEM;

        double pesoCobrado = Math.max(etiqueta.getPesoKg(), pesoCubado);

        Transportadora t = etiqueta.getTransportadora();
        double valorFrete = t.getPrecoBase() + (pesoCobrado * t.getPrecoPorKg());

        if (etiqueta.getValorDeclarado() > 0) {
            valorFrete += etiqueta.getValorDeclarado() * 0.01;
        }

        int prazoDias = (t == Transportadora.CORREIOS_SEDEX) ? 2 : 7;

        return new CalculoFreteResponseDTO(t, pesoCobrado, arredondar(valorFrete), prazoDias);
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
