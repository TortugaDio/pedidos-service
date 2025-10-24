package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FechaEntregaValidatorTest {

    private FechaEntregaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FechaEntregaValidator();
    }

    @Test
    void debeValidarFechaFutura() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .fechaEntrega(LocalDate.now().plusDays(1))
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeValidarFechaActual() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .fechaEntrega(LocalDate.now())
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeRechazarFechaPasada() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .fechaEntrega(LocalDate.now().minusDays(1))
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("FECHA_ENTREGA_INVALIDA", resultado.get().getTipoError());
    }

    @Test
    void debeRechazarFechaNula() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .fechaEntrega(null)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isPresent());
    }
}
