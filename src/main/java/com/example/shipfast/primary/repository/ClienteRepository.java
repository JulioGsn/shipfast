package com.example.shipfast.primary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shipfast.primary.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
