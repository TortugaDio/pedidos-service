package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zonas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaEntity {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String codigoZona;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private boolean soporteRefrigeracion;

    @Column(nullable = false)
    private boolean activa;
}
