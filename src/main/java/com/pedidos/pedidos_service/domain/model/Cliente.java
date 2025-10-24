package com.pedidos.pedidos_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private String clienteId;
    private String nombre;
    private String email;
    private boolean activo;
}
