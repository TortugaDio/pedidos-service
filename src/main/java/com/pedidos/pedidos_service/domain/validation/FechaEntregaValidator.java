package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FechaEntregaValidator implements PedidoValidator {

    private static final String TIPO_ERROR = "FECHA_ENTREGA_INVALIDA";

    @Override
    public Optional<ErrorValidacion> validate(Pedido pedido, int fila) {
        if (!pedido.tieneFechaEntregaValida()) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError(TIPO_ERROR)
                    .mensaje("La fecha de entrega no puede ser una fecha pasada")
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public String getTipoError() {
        return TIPO_ERROR;
    }
}