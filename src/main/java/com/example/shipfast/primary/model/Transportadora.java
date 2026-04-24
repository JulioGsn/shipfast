package com.example.shipfast.primary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transportadora")
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "bean_calculadora", nullable = false)
    private String beanCalculadora;

    @Column(name = "peso_maximo_kg", nullable = false)
    private double pesoMaximoKg;

    @Column(name = "dimensao_maxima_cm", nullable = false)
    private int dimensaoMaximaCm;

    @Column(name = "soma_dimensoes_maxima_cm", nullable = false)
    private int somaDimensoesMaximaCm;

    @Column(name = "preco_base", nullable = false)
    private double precoBase;

    @Column(name = "preco_por_kg", nullable = false)
    private double precoPorKg;

    public Transportadora() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBeanCalculadora() {
        return beanCalculadora;
    }

    public void setBeanCalculadora(String beanCalculadora) {
        this.beanCalculadora = beanCalculadora;
    }

    public double getPesoMaximoKg() {
        return pesoMaximoKg;
    }

    public void setPesoMaximoKg(double pesoMaximoKg) {
        this.pesoMaximoKg = pesoMaximoKg;
    }

    public int getDimensaoMaximaCm() {
        return dimensaoMaximaCm;
    }

    public void setDimensaoMaximaCm(int dimensaoMaximaCm) {
        this.dimensaoMaximaCm = dimensaoMaximaCm;
    }

    public int getSomaDimensoesMaximaCm() {
        return somaDimensoesMaximaCm;
    }

    public void setSomaDimensoesMaximaCm(int somaDimensoesMaximaCm) {
        this.somaDimensoesMaximaCm = somaDimensoesMaximaCm;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public double getPrecoPorKg() {
        return precoPorKg;
    }

    public void setPrecoPorKg(double precoPorKg) {
        this.precoPorKg = precoPorKg;
    }
}
