package com.example.shipfast.primary.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "etiqueta")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_rastreio", nullable = false, unique = true)
    private String codigoRastreio;

    @Column(name = "cep_origem", nullable = false)
    private String cepOrigem;

    @Column(name = "cep_destino", nullable = false)
    private String cepDestino;

    @Column(name = "nome_destinatario", nullable = false)
    private String nomeDestinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEtiqueta status;

    @Column(name = "valor_frete", nullable = false)
    private double valorFrete;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportadora_id", nullable = false)
    private Transportadora transportadora;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "etiqueta_pacote",
            joinColumns = @JoinColumn(name = "etiqueta_id"),
            inverseJoinColumns = @JoinColumn(name = "pacote_id")
    )
    private Set<Pacote> pacotes = new HashSet<>();

    public Etiqueta() {
    }

    @PrePersist
    void prePersist() {
        if (criadaEm == null) {
            criadaEm = LocalDateTime.now();
        }
        if (status == null) {
            status = StatusEtiqueta.CRIADA;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

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

    public StatusEtiqueta getStatus() {
        return status;
    }

    public void setStatus(StatusEtiqueta status) {
        this.status = status;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Set<Pacote> getPacotes() {
        return pacotes;
    }

    public void setPacotes(Set<Pacote> pacotes) {
        this.pacotes = pacotes;
    }

    public int somaDimensoesMaximas() {
        return pacotes.stream().mapToInt(Pacote::somaDimensoes).max().orElse(0);
    }

    public double pesoTotalKg() {
        return pacotes.stream().mapToDouble(Pacote::getPesoKg).sum();
    }
}
