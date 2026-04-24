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

import com.example.shipfast.primary.dto.PacoteRequestDTO;
import com.example.shipfast.primary.dto.PacoteResponseDTO;
import com.example.shipfast.primary.service.PacoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacotes")
public class PacoteController {

    private final PacoteService service;

    public PacoteController(PacoteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PacoteResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacoteResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<PacoteResponseDTO> criar(@Valid @RequestBody PacoteRequestDTO dto) {
        PacoteResponseDTO criado = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/api/pacotes/" + criado.id()))
                .body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacoteResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PacoteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
