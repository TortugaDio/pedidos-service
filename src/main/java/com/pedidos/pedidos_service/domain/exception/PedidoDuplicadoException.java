package com.pedidos.pedidos_service.domain.exception;

public class PedidoDuplicadoException extends DomainException {
    public PedidoDuplicadoException(String numeroPedido) {
        super("El número de pedido ya existe: " + numeroPedido);
    }
}
