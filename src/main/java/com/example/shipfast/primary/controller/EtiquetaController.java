package com.example.shipfast.primary.controller;

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

import com.example.shipfast.primary.dto.EtiquetaRequestDTO;
import com.example.shipfast.primary.dto.EtiquetaResponseDTO;
import com.example.shipfast.primary.model.StatusEtiqueta;
import com.example.shipfast.primary.service.EtiquetaService;

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
            @RequestParam(required = false) StatusEtiqueta status) {
        List<EtiquetaResponseDTO> resposta = (status != null)
                ? service.buscarPorStatus(status)
                : service.listar();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponseDTO> criar(@Valid @RequestBody EtiquetaRequestDTO dto) {
        EtiquetaResponseDTO criada = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/api/etiquetas/" + criada.id()))
                .body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody EtiquetaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
