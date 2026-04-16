package com.example.shipfast.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.shipfast.model.Etiqueta;
import com.example.shipfast.model.StatusEtiqueta;
import com.example.shipfast.model.Transportadora;

@Repository
public class EtiquetaRepository {

    private final Map<Long, Etiqueta> banco = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public List<Etiqueta> findAll() {
        return new ArrayList<>(banco.values());
    }

    public Optional<Etiqueta> findById(Long id) {
        return Optional.ofNullable(banco.get(id));
    }

    public Etiqueta save(Etiqueta etiqueta) {
        if (etiqueta.getId() == null) {
            etiqueta.setId(sequencia.getAndIncrement());
        }
        banco.put(etiqueta.getId(), etiqueta);
        return etiqueta;
    }

    public boolean deleteById(Long id) {
        return banco.remove(id) != null;
    }

    public List<Etiqueta> buscarPorTransportadora(Transportadora transportadora) {
        return banco.values().stream()
                .filter(e -> e.getTransportadora() == transportadora)
                .toList();
    }

    public List<Etiqueta> buscarPorStatus(StatusEtiqueta status) {
        return banco.values().stream()
                .filter(e -> e.getStatus() == status)
                .toList();
    }
}
