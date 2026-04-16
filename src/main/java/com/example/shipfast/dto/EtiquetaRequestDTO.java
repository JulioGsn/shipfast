package com.example.shipfast.dto;

import com.example.shipfast.model.Transportadora;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class EtiquetaRequestDTO {

    @NotBlank(message = "CEP de origem é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP de origem deve conter 8 dígitos")
    private String cepOrigem;

    @NotBlank(message = "CEP de destino é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP de destino deve conter 8 dígitos")
    private String cepDestino;

    @NotBlank(message = "Nome do destinatário é obrigatório")
    private String nomeDestinatario;

    @DecimalMin(value = "0.1", message = "Peso mínimo é 0.1 kg")
    private double pesoKg;

    @Min(value = 1, message = "Altura deve ser pelo menos 1 cm")
    private int alturaCm;

    @Min(value = 1, message = "Largura deve ser pelo menos 1 cm")
    private int larguraCm;

    @Min(value = 1, message = "Comprimento deve ser pelo menos 1 cm")
    private int comprimentoCm;

    @PositiveOrZero(message = "Valor declarado não pode ser negativo")
    private double valorDeclarado;

    @NotNull(message = "Transportadora é obrigatória")
    private Transportadora transportadora;

    public String getCepOrigem() {
        return cepOrigem;
    }

    public void setCepOrigem(String cepOrigem) {
        this.cepOrigem = cepOrigem;
    }

    public String getCepDestino() {
        return cepDestino;
    }

    public void setCepDestino(String cepDestino) {
        this.cepDestino = cepDestino;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
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

    public double getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(double valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }
}
