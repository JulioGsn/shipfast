package com.example.shipfast.primary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shipfast.audit.model.TipoOperacao;
import com.example.shipfast.audit.service.AuditoriaService;
import com.example.shipfast.exception.RecursoNaoEncontradoException;
import com.example.shipfast.primary.dto.ClienteRequestDTO;
import com.example.shipfast.primary.dto.ClienteResponseDTO;
import com.example.shipfast.primary.model.Cliente;
import com.example.shipfast.primary.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final AuditoriaService auditoria;

    public ClienteService(ClienteRepository repository, AuditoriaService auditoria) {
        this.repository = repository;
        this.auditoria = auditoria;
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public ClienteResponseDTO buscar(Long id) {
        return toResponse(buscarEntity(id));
    }

    @Transactional("primaryTransactionManager")
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setCpfCnpj(dto.cpfCnpj());
        Cliente salvo = repository.save(cliente);
        auditoria.registrar("Cliente", salvo.getId(), TipoOperacao.CRIADO,
                "cliente criado: " + salvo.getNome());
        return toResponse(salvo);
    }

    @Transactional("primaryTransactionManager")
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntity(id);
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setCpfCnpj(dto.cpfCnpj());
        Cliente salvo = repository.save(cliente);
        auditoria.registrar("Cliente", salvo.getId(), TipoOperacao.ATUALIZADO,
                "cliente atualizado: " + salvo.getNome());
        return toResponse(salvo);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Cliente cliente = buscarEntity(id);
        repository.delete(cliente);
        auditoria.registrar("Cliente", id, TipoOperacao.REMOVIDO,
                "cliente removido: " + cliente.getNome());
    }

    Cliente buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente com id " + id + " não encontrado"));
    }

    private ClienteResponseDTO toResponse(Cliente c) {
        return new ClienteResponseDTO(c.getId(), c.getNome(), c.getEmail(), c.getCpfCnpj());
    }
}
