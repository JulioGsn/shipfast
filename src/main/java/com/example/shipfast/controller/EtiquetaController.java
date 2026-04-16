package com.example.shipfast.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipfast.dto.CalculoFreteResponseDTO;
import com.example.shipfast.dto.EtiquetaRequestDTO;
import com.example.shipfast.dto.EtiquetaResponseDTO;
import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.Transportadora;
import com.example.shipfast.service.EtiquetaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/etiquetas")
public class EtiquetaController {

    private final EtiquetaService service;

    public EtiquetaController(EtiquetaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EtiquetaResponseDTO>> listar(
            @RequestParam(required = false) Transportadora transportadora) {

        List<Etiqueta> etiquetas = (transportadora != null)
                ? service.buscarPorTransportadora(transportadora)
                : service.listar();

        List<EtiquetaResponseDTO> resposta = etiquetas.stream()
                .map(EtiquetaResponseDTO::from)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> buscar(@PathVariable Long id) {
        Etiqueta etiqueta = service.buscar(id);
        return ResponseEntity.ok(EtiquetaResponseDTO.from(etiqueta));
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponseDTO> criar(@Valid @RequestBody EtiquetaRequestDTO dto) {
        Etiqueta criada = service.criar(dto);
        URI location = URI.create("/api/etiquetas/" + criada.getId());
        return ResponseEntity.created(location).body(EtiquetaResponseDTO.from(criada));
    }

    @PostMapping("/calcular")
    public ResponseEntity<CalculoFreteResponseDTO> calcular(
            @Valid @RequestBody EtiquetaRequestDTO dto) {
        return ResponseEntity.ok(service.calcularPreview(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EtiquetaRequestDTO dto) {
        Etiqueta atualizada = service.atualizar(id, dto);
        return ResponseEntity.ok(EtiquetaResponseDTO.from(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
