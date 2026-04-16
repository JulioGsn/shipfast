package com.example.shipfast.dto;

import java.time.LocalDateTime;

import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.StatusEtiqueta;
import com.example.shipfast.model.Transportadora;

public class EtiquetaResponseDTO {

    private Long id;
    private String codigoRastreio;
    private String cepOrigem;
    private String cepDestino;
    private String nomeDestinatario;
    private double pesoKg;
    private int alturaCm;
    private int larguraCm;
    private int comprimentoCm;
    private Transportadora transportadora;
    private StatusEtiqueta status;
    private double valorFrete;
    private LocalDateTime criadaEm;

    public static EtiquetaResponseDTO from(Etiqueta e) {
        EtiquetaResponseDTO dto = new EtiquetaResponseDTO();
        dto.id = e.getId();
        dto.codigoRastreio = e.getCodigoRastreio();
        dto.cepOrigem = e.getCepOrigem();
        dto.cepDestino = e.getCepDestino();
        dto.nomeDestinatario = e.getNomeDestinatario();
        dto.pesoKg = e.getPesoKg();
        dto.alturaCm = e.getAlturaCm();
        dto.larguraCm = e.getLarguraCm();
        dto.comprimentoCm = e.getComprimentoCm();
        dto.transportadora = e.getTransportadora();
        dto.status = e.getStatus();
        dto.valorFrete = e.getValorFrete();
        dto.criadaEm = e.getCriadaEm();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public String getCepOrigem() {
        return cepOrigem;
    }

    public String getCepDestino() {
        return cepDestino;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public int getAlturaCm() {
        return alturaCm;
    }

    public int getLarguraCm() {
        return larguraCm;
    }

    public int getComprimentoCm() {
        return comprimentoCm;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public StatusEtiqueta getStatus() {
        return status;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }
}
