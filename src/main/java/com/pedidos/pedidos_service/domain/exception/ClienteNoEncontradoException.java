package com.pedidos.pedidos_service.domain.exception;

public class ClienteNoEncontradoException extends DomainException {
    public ClienteNoEncontradoException(String clienteId) {
        super("Cliente no encontrado: " + clienteId);
    }
}
