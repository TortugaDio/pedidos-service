package com.pedidos.pedidos_service.application.ports.output;

import com.pedidos.pedidos_service.domain.model.Cliente;

import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> findByClienteId(String clienteId);
    boolean existsByClienteId(String clienteId);
}
