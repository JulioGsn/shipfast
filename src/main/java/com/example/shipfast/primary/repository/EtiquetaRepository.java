package com.example.shipfast.primary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shipfast.primary.model.Etiqueta;
import com.example.shipfast.primary.model.StatusEtiqueta;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    @Query("SELECT e FROM Etiqueta e WHERE e.status = :status")
    List<Etiqueta> buscarPorStatus(@Param("status") StatusEtiqueta status);

    @Query("""
            SELECT DISTINCT e FROM Etiqueta e
            LEFT JOIN FETCH e.cliente
            LEFT JOIN FETCH e.transportadora
            LEFT JOIN FETCH e.pacotes
            WHERE e.id = :id
            """)
    Optional<Etiqueta> buscarPorIdComDetalhes(@Param("id") Long id);
}
