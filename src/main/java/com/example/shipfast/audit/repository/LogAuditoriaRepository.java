package com.example.shipfast.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipfast.audit.model.LogAuditoria;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
}
