package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.domain.model.Zona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefrigeracionZonaValidatorTest {

    private RefrigeracionZonaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RefrigeracionZonaValidator();
    }

    @Test
    void debeValidarPedidoConRefrigeracionEnZonaCompatible() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .requiereRefrigeracion(true)
                .build();

        Zona zona = Zona.builder()
                .codigoZona("ZONA1")
                .soporteRefrigeracion(true)
                .activa(true)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, zona, 1);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeValidarPedidoSinRefrigeracion() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .requiereRefrigeracion(false)
                .build();

        Zona zona = Zona.builder()
                .codigoZona("ZONA3")
                .soporteRefrigeracion(false)
                .activa(true)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, zona, 1);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeRechazarPedidoConRefrigeracionEnZonaIncompatible() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .requiereRefrigeracion(true)
                .build();

        Zona zona = Zona.builder()
                .codigoZona("ZONA3")
                .soporteRefrigeracion(false)
                .activa(true)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, zona, 1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("ZONA_SIN_SOPORTE_REFRIGERACION", resultado.get().getTipoError());
    }

    @Test
    void debeRechazarPedidoEnZonaInactiva() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .requiereRefrigeracion(true)
                .build();

        Zona zona = Zona.builder()
                .codigoZona("ZONA1")
                .soporteRefrigeracion(true)
                .activa(false)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, zona, 1);

        // Assert
        assertTrue(resultado.isPresent());
    }
}
