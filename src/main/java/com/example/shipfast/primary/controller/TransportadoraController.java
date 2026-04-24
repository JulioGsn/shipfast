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
import org.springframework.web.bind.annotation.RestController;

import com.example.shipfast.primary.dto.TransportadoraRequestDTO;
import com.example.shipfast.primary.dto.TransportadoraResponseDTO;
import com.example.shipfast.primary.repository.TransportadoraRanking;
import com.example.shipfast.primary.service.TransportadoraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transportadoras")
public class TransportadoraController {

    private final TransportadoraService service;

    public TransportadoraController(TransportadoraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransportadoraResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<TransportadoraRanking>> ranking() {
        return ResponseEntity.ok(service.ranking());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadoraResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<TransportadoraResponseDTO> criar(@Valid @RequestBody TransportadoraRequestDTO dto) {
        TransportadoraResponseDTO criada = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/api/transportadoras/" + criada.id()))
                .body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportadoraResponseDTO> atualizar(@PathVariable Long id,
                                                               @Valid @RequestBody TransportadoraRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
