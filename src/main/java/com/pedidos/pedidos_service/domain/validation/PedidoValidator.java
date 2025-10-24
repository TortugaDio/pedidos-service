package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;

import java.util.Optional;

public interface PedidoValidator {
    Optional<ErrorValidacion> validate(Pedido pedido, int fila);
    String getTipoError();
}
