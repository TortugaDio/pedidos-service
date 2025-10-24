package com.pedidos.pedidos_service.application.ports.output;

import com.pedidos.pedidos_service.domain.model.Pedido;

import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido save(Pedido pedido);
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    boolean existsByNumeroPedido(String numeroPedido);
}
