package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence;

import com.pedidos.pedidos_service.application.ports.output.ClienteRepositoryPort;
import com.pedidos.pedidos_service.domain.model.Cliente;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ClienteJpaRepository;
import com.pedidos.pedidos_service.infrastructure.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;
    private final ClienteMapper mapper;

    @Override
    public Optional<Cliente> findByClienteId(String clienteId) {
        return jpaRepository.findByClienteId(clienteId)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByClienteId(String clienteId) {
        return jpaRepository.existsByClienteId(clienteId);
    }
}
