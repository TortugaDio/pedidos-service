package com.pedidos.pedidos_service.domain.validation;

import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumeroPedidoValidatorTest {

    private NumeroPedidoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NumeroPedidoValidator();
    }

    @Test
    void debeValidarNumeroPedidoValido() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isEmpty(), "No debe haber error para número válido");
    }

    @Test
    void debeRechazarNumeroPedidoConEspacios() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("P 001")
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("NUMERO_PEDIDO_INVALIDO", resultado.get().getTipoError());
    }

    @Test
    void debeRechazarNumeroPedidoVacio() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido("")
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isPresent());
    }

    @Test
    void debeRechazarNumeroPedidoNulo() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .numeroPedido(null)
                .build();

        // Act
        Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);

        // Assert
        assertTrue(resultado.isPresent());
    }
}
