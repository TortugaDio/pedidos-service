package com.pedidos.pedidos_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorValidacionDTO {
    private int fila;
    private String numeroPedido;
    private String tipoError;
    private String mensaje;
}
