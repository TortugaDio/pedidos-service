package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EstadoPedidoValidator implements PedidoValidator {

    private static final String TIPO_ERROR = "ESTADO_INVALIDO";

    @Override
    public Optional<ErrorValidacion> validate(Pedido pedido, int fila) {
        if (pedido.getEstado() == null) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError(TIPO_ERROR)
                    .mensaje("El estado debe ser PENDIENTE, CONFIRMADO o ENTREGADO")
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public String getTipoError() {
        return TIPO_ERROR;
    }
}