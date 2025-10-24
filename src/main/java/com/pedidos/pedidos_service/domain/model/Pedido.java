package com.pedidos.pedidos_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private Long id;
    private String numeroPedido;
    private String clienteId;
    private LocalDate fechaEntrega;
    private EstadoPedido estado;
    private String zonaEntrega;
    private boolean requiereRefrigeracion;

    public boolean tieneFechaEntregaValida() {
        return fechaEntrega != null && !fechaEntrega.isBefore(LocalDate.now());
    }

    public boolean tieneNumeroValido() {
        return numeroPedido != null &&
                numeroPedido.matches("^[A-Za-z0-9]+$") &&
                !numeroPedido.isEmpty();
    }
}
