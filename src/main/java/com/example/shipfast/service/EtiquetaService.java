package com.example.shipfast.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.shipfast.dto.CalculoFreteResponseDTO;
import com.example.shipfast.dto.EtiquetaRequestDTO;
import com.example.shipfast.exception.RecursoNaoEncontradoException;
import com.example.shipfast.exception.RegraNegocioException;
import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.StatusEtiqueta;
import com.example.shipfast.model.Transportadora;
import com.example.shipfast.repository.EtiquetaRepository;

@Service
public class EtiquetaService {

    private final EtiquetaRepository repository;
    private final CalculadoraFreteService calculadoraPadrao;
    private final Map<String, CalculadoraFreteService> calculadoras;

    @Autowired
    public EtiquetaService(EtiquetaRepository repository,
                           @Qualifier("correios") CalculadoraFreteService calculadoraPadrao,
                           Map<String, CalculadoraFreteService> calculadoras) {
        this.repository = repository;
        this.calculadoraPadrao = calculadoraPadrao;
        this.calculadoras = calculadoras;
    }

    public List<Etiqueta> listar() {
        return repository.findAll();
    }

    public Etiqueta buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Etiqueta com id " + id + " não encontrada"));
    }

    public List<Etiqueta> buscarPorTransportadora(Transportadora transportadora) {
        return repository.buscarPorTransportadora(transportadora);
    }

    public Etiqueta criar(EtiquetaRequestDTO dto) {
        Etiqueta etiqueta = montar(dto);

        CalculadoraFreteService calculadora = escolherCalculadora(etiqueta.getTransportadora());
        CalculoFreteResponseDTO calculo = calculadora.calcular(etiqueta);

        etiqueta.setValorFrete(calculo.getValorFrete());
        etiqueta.setCodigoRastreio(gerarCodigoRastreio(etiqueta.getTransportadora()));
        etiqueta.setStatus(StatusEtiqueta.CRIADA);
        etiqueta.setCriadaEm(LocalDateTime.now());

        return repository.save(etiqueta);
    }

    public Etiqueta atualizar(Long id, EtiquetaRequestDTO dto) {
        Etiqueta existente = buscar(id);

        if (existente.getStatus() == StatusEtiqueta.POSTADA
                || existente.getStatus() == StatusEtiqueta.ENTREGUE) {
            throw new RegraNegocioException(
                    "Etiqueta já postada ou entregue não pode ser alterada");
        }

        existente.setCepOrigem(dto.getCepOrigem());
        existente.setCepDestino(dto.getCepDestino());
        existente.setNomeDestinatario(dto.getNomeDestinatario());
        existente.setPesoKg(dto.getPesoKg());
        existente.setAlturaCm(dto.getAlturaCm());
        existente.setLarguraCm(dto.getLarguraCm());
        existente.setComprimentoCm(dto.getComprimentoCm());
        existente.setValorDeclarado(dto.getValorDeclarado());
        existente.setTransportadora(dto.getTransportadora());

        CalculadoraFreteService calculadora = escolherCalculadora(existente.getTransportadora());
        CalculoFreteResponseDTO calculo = calculadora.calcular(existente);
        existente.setValorFrete(calculo.getValorFrete());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        Etiqueta existente = buscar(id);

        if (existente.getStatus() == StatusEtiqueta.POSTADA
                || existente.getStatus() == StatusEtiqueta.ENTREGUE) {
            throw new RegraNegocioException(
                    "Etiqueta já postada ou entregue não pode ser excluída");
        }

        repository.deleteById(id);
    }

    public CalculoFreteResponseDTO calcularPreview(EtiquetaRequestDTO dto) {
        Etiqueta etiqueta = montar(dto);
        CalculadoraFreteService calculadora = escolherCalculadora(etiqueta.getTransportadora());
        return calculadora.calcular(etiqueta);
    }

    private CalculadoraFreteService escolherCalculadora(Transportadora transportadora) {
        String nomeBean = transportadora.getBeanCalculadora();
        return calculadoras.getOrDefault(nomeBean, calculadoraPadrao);
    }

    private Etiqueta montar(EtiquetaRequestDTO dto) {
        Etiqueta e = new Etiqueta();
        e.setCepOrigem(dto.getCepOrigem());
        e.setCepDestino(dto.getCepDestino());
        e.setNomeDestinatario(dto.getNomeDestinatario());
        e.setPesoKg(dto.getPesoKg());
        e.setAlturaCm(dto.getAlturaCm());
        e.setLarguraCm(dto.getLarguraCm());
        e.setComprimentoCm(dto.getComprimentoCm());
        e.setValorDeclarado(dto.getValorDeclarado());
        e.setTransportadora(dto.getTransportadora());
        return e;
    }

    private String gerarCodigoRastreio(Transportadora transportadora) {
        String prefixo = switch (transportadora) {
            case CORREIOS_PAC -> "BR-PAC";
            case CORREIOS_SEDEX -> "BR-SDX";
            case JADLOG -> "JAD";
        };
        return prefixo + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
