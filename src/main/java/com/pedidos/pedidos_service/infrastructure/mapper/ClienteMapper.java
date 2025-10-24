package com.pedidos.pedidos_service.infrastructure.mapper;

import com.pedidos.pedidos_service.domain.model.Cliente;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteEntity toEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return ClienteEntity.builder()
                .clienteId(cliente.getClienteId())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .activo(cliente.isActivo())
                .build();
    }

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }

        return Cliente.builder()
                .clienteId(entity.getClienteId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .activo(entity.isActivo())
                .build();
    }
}
