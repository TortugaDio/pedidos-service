package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository;

import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
    Optional<PedidoEntity> findByNumeroPedido(String numeroPedido);
    boolean existsByNumeroPedido(String numeroPedido);
}