package com.pedidos.pedidos_service.domain.exception;

public class ArchivoInvalidoException extends DomainException {
    public ArchivoInvalidoException(String message) {
        super(message);
    }

    public ArchivoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
