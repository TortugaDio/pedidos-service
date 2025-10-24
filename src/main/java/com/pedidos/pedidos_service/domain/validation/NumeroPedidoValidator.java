package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NumeroPedidoValidator implements PedidoValidator {

    private static final String TIPO_ERROR = "NUMERO_PEDIDO_INVALIDO";

    @Override
    public Optional<ErrorValidacion> validate(Pedido pedido, int fila) {
        if (!pedido.tieneNumeroValido()) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError(TIPO_ERROR)
                    .mensaje("El número de pedido debe ser alfanumérico y no vacío")
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public String getTipoError() {
        return TIPO_ERROR;
    }
}