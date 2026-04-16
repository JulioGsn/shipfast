package com.example.shipfast.service.impl;

import org.springframework.stereotype.Service;

import com.example.shipfast.dto.CalculoFreteResponseDTO;
import com.example.shipfast.exception.RegraNegocioException;
import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.Transportadora;
import com.example.shipfast.service.CalculadoraFreteService;

@Service("jadlog")
public class JadlogCalculadoraService implements CalculadoraFreteService {

    private static final double FATOR_CUBAGEM = 5000.0;

    @Override
    public void validarLimites(Etiqueta etiqueta) {
        Transportadora t = etiqueta.getTransportadora();

        if (t != Transportadora.JADLOG) {
            throw new RegraNegocioException(
                    "Calculadora Jadlog recebeu transportadora incompatível: " + t.name());
        }

        if (etiqueta.getPesoKg() > t.getPesoMaximoKg()) {
            throw new RegraNegocioException(
                    "Jadlog não aceita pacote acima de " + t.getPesoMaximoKg() + " kg");
        }

        if (etiqueta.somaDimensoes() > t.getSomaDimensoesMaximaCm()) {
            throw new RegraNegocioException(
                    "Jadlog exige soma de dimensões até "
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

        int distanciaFaixa = calcularFaixaDistancia(etiqueta.getCepOrigem(), etiqueta.getCepDestino());
        valorFrete += distanciaFaixa * 3.50;

        int prazoDias = 3 + distanciaFaixa;

        return new CalculoFreteResponseDTO(t, pesoCobrado, arredondar(valorFrete), prazoDias);
    }

    private int calcularFaixaDistancia(String cepOrigem, String cepDestino) {
        int regiaoOrigem = Integer.parseInt(cepOrigem.substring(0, 1));
        int regiaoDestino = Integer.parseInt(cepDestino.substring(0, 1));
        return Math.abs(regiaoOrigem - regiaoDestino);
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
