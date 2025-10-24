package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.domain.model.Zona;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefrigeracionZonaValidator {

    private static final String TIPO_ERROR = "ZONA_SIN_SOPORTE_REFRIGERACION";

    public Optional<ErrorValidacion> validate(Pedido pedido, Zona zona, int fila) {
        if (pedido.isRequiereRefrigeracion() && !zona.puedeRecibirPedidoRefrigerado()) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError(TIPO_ERROR)
                    .mensaje(String.format(
                            "La zona %s no soporta refrigeración requerida por el pedido",
                            zona.getCodigoZona()
                    ))
                    .build());
        }
        return Optional.empty();
    }

    public String getTipoError() {
        return TIPO_ERROR;
    }
}
