package com.example.shipfast.primary.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacote")
public class Pacote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPacote categoria;

    @Column(name = "valor_declarado", nullable = false)
    private double valorDeclarado;

    @Column(name = "peso_kg", nullable = false)
    private double pesoKg;

    @Column(name = "altura_cm", nullable = false)
    private int alturaCm;

    @Column(name = "largura_cm", nullable = false)
    private int larguraCm;

    @Column(name = "comprimento_cm", nullable = false)
    private int comprimentoCm;

    @ManyToMany(mappedBy = "pacotes")
    private Set<Etiqueta> etiquetas = new HashSet<>();

    public Pacote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaPacote getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaPacote categoria) {
        this.categoria = categoria;
    }

    public double getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(double valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public int getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(int alturaCm) {
        this.alturaCm = alturaCm;
    }

    public int getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(int larguraCm) {
        this.larguraCm = larguraCm;
    }

    public int getComprimentoCm() {
        return comprimentoCm;
    }

    public void setComprimentoCm(int comprimentoCm) {
        this.comprimentoCm = comprimentoCm;
    }

    public Set<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Set<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public int somaDimensoes() {
        return alturaCm + larguraCm + comprimentoCm;
    }
}
