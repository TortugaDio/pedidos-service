package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository;

import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, String> {
    Optional<ClienteEntity> findByClienteId(String clienteId);
    boolean existsByClienteId(String clienteId);
}