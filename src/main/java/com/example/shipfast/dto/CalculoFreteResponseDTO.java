package com.example.shipfast.dto;

import com.example.shipfast.model.Transportadora;

public class CalculoFreteResponseDTO {

    private Transportadora transportadora;
    private double pesoCobradoKg;
    private double valorFrete;
    private int prazoEstimadoDias;

    public CalculoFreteResponseDTO(Transportadora transportadora,
                                   double pesoCobradoKg,
                                   double valorFrete,
                                   int prazoEstimadoDias) {
        this.transportadora = transportadora;
        this.pesoCobradoKg = pesoCobradoKg;
        this.valorFrete = valorFrete;
        this.prazoEstimadoDias = prazoEstimadoDias;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public double getPesoCobradoKg() {
        return pesoCobradoKg;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public int getPrazoEstimadoDias() {
        return prazoEstimadoDias;
    }
}
