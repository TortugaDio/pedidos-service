package com.pedidos.pedidos_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zona {
    private String codigoZona;
    private String nombre;
    private boolean soporteRefrigeracion;
    private boolean activa;

    public boolean puedeRecibirPedidoRefrigerado() {
        return this.soporteRefrigeracion && this.activa;
    }
}