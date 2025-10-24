package com.pedidos.pedidos_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorValidacion {
    private int fila;
    private String numeroPedido;
    private String tipoError;
    private String mensaje;
}
