package com.pedidos.pedidos_service.domain.exception;

public class ZonaNoEncontradaException extends DomainException {
    public ZonaNoEncontradaException(String zonaId) {
        super("Zona no encontrada: " + zonaId);
    }
}
