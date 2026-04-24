package com.example.shipfast.audit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipfast.audit.dto.LogAuditoriaResponseDTO;
import com.example.shipfast.audit.service.AuditoriaService;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LogAuditoriaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}
