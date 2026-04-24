package com.example.shipfast.primary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shipfast.primary.model.Transportadora;

public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {

    @Query(
            value = """
                    SELECT t.id AS id,
                           t.nome AS nome,
                           COUNT(e.id) AS totalEtiquetas
                    FROM transportadora t
                    LEFT JOIN etiqueta e ON e.transportadora_id = t.id
                    GROUP BY t.id, t.nome
                    ORDER BY totalEtiquetas DESC
                    """,
            nativeQuery = true
    )
    List<TransportadoraRanking> rankingUso();
}
