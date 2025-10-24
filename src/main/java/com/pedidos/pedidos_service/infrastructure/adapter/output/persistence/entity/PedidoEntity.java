package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity;


import com.pedidos.pedidos_service.domain.model.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pedidos", indexes = {
        @Index(name = "idx_numero_pedido", columnList = "numeroPedido", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroPedido;

    @Column(nullable = false, length = 50)
    private String clienteId;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    @Column(nullable = false, length = 50)
    private String zonaEntrega;

    @Column(nullable = false)
    private boolean requiereRefrigeracion;
}