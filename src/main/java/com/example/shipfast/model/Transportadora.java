package com.example.shipfast.model;

public enum Transportadora {

    CORREIOS_PAC("correios", 30.0, 100, 200, 18.50, 0.90),
    CORREIOS_SEDEX("correios", 30.0, 105, 200, 29.90, 1.80),
    JADLOG("jadlog", 70.0, 150, 300, 24.00, 1.20);

    private final String beanCalculadora;
    private final double pesoMaximoKg;
    private final int dimensaoMaximaCm;
    private final int somaDimensoesMaximaCm;
    private final double precoBase;
    private final double precoPorKg;

    Transportadora(String beanCalculadora,
                   double pesoMaximoKg,
                   int dimensaoMaximaCm,
                   int somaDimensoesMaximaCm,
                   double precoBase,
                   double precoPorKg) {
        this.beanCalculadora = beanCalculadora;
        this.pesoMaximoKg = pesoMaximoKg;
        this.dimensaoMaximaCm = dimensaoMaximaCm;
        this.somaDimensoesMaximaCm = somaDimensoesMaximaCm;
        this.precoBase = precoBase;
        this.precoPorKg = precoPorKg;
    }

    public String getBeanCalculadora() {
        return beanCalculadora;
    }

    public double getPesoMaximoKg() {
        return pesoMaximoKg;
    }

    public int getDimensaoMaximaCm() {
        return dimensaoMaximaCm;
    }

    public int getSomaDimensoesMaximaCm() {
        return somaDimensoesMaximaCm;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public double getPrecoPorKg() {
        return precoPorKg;
    }
}
