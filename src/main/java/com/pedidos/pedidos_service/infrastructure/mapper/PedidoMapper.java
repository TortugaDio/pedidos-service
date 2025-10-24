package com.pedidos.pedidos_service.infrastructure.mapper;

import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoEntity toEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        return PedidoEntity.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .clienteId(pedido.getClienteId())
                .fechaEntrega(pedido.getFechaEntrega())
                .estado(pedido.getEstado())
                .zonaEntrega(pedido.getZonaEntrega())
                .requiereRefrigeracion(pedido.isRequiereRefrigeracion())
                .build();
    }

    public Pedido toDomain(PedidoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Pedido.builder()
                .id(entity.getId())
                .numeroPedido(entity.getNumeroPedido())
                .clienteId(entity.getClienteId())
                .fechaEntrega(entity.getFechaEntrega())
                .estado(entity.getEstado())
                .zonaEntrega(entity.getZonaEntrega())
                .requiereRefrigeracion(entity.isRequiereRefrigeracion())
                .build();
    }
}
