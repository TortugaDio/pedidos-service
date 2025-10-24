package com.pedidos.pedidos_service.domain.model;

public enum EstadoPedido {
    PENDIENTE,
    CONFIRMADO,
    ENTREGADO;

    public static boolean esValido(String estado) {
        try {
            EstadoPedido.valueOf(estado.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
