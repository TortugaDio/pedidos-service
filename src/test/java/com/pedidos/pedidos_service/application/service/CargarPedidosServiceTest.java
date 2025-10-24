package com.pedidos.pedidos_service.application.service;

import com.pedidos.pedidos_service.application.ports.output.ClienteRepositoryPort;
import com.pedidos.pedidos_service.application.ports.output.CsvParserPort;
import com.pedidos.pedidos_service.application.ports.output.PedidoRepositoryPort;
import com.pedidos.pedidos_service.application.ports.output.ZonaRepositoryPort;
import com.pedidos.pedidos_service.domain.model.EstadoPedido;
import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.domain.model.ResultadoProcesamiento;
import com.pedidos.pedidos_service.domain.model.Zona;
import com.pedidos.pedidos_service.domain.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargarPedidosServiceTest {

    @Mock
    private CsvParserPort csvParser;

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private ZonaRepositoryPort zonaRepository;

    @Mock
    private MultipartFile archivo;

    private CargarPedidosService service;

    @BeforeEach
    void setUp() {
        List<PedidoValidator> validators = Arrays.asList(
                new NumeroPedidoValidator(),
                new FechaEntregaValidator(),
                new EstadoPedidoValidator()
        );

        RefrigeracionZonaValidator refrigeracionValidator = new RefrigeracionZonaValidator();

        service = new CargarPedidosService(
                csvParser,
                pedidoRepository,
                clienteRepository,
                zonaRepository,
                validators,
                refrigeracionValidator
        );
    }

    @Test
    void debeProcesarPedidosValidosCorrectamente() {
        // Arrange
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("pedidos.csv");

        Pedido pedidoValido = crearPedidoValido("P001", "CLI-123", "ZONA1", true);
        when(csvParser.parse(archivo)).thenReturn(List.of(pedidoValido));

        when(pedidoRepository.existsByNumeroPedido(anyString())).thenReturn(false);
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(true);
        when(zonaRepository.findByCodigoZona(anyString()))
                .thenReturn(Optional.of(crearZona("ZONA1", true)));
        when(pedidoRepository.save(any())).thenReturn(pedidoValido);

        // Act
        ResultadoProcesamiento resultado = service.cargarPedidos(archivo);

        // Assert
        assertEquals(1, resultado.getTotalProcesados());
        assertEquals(1, resultado.getTotalGuardados());
        assertEquals(0, resultado.getTotalErrores());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void debeRechazarPedidoConClienteInexistente() {
        // Arrange
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("pedidos.csv");

        Pedido pedido = crearPedidoValido("P001", "CLI-999", "ZONA1", false);
        when(csvParser.parse(archivo)).thenReturn(List.of(pedido));

        when(pedidoRepository.existsByNumeroPedido(anyString())).thenReturn(false);
        when(clienteRepository.existsByClienteId("CLI-999")).thenReturn(false);

        // Act
        ResultadoProcesamiento resultado = service.cargarPedidos(archivo);

        // Assert
        assertEquals(1, resultado.getTotalProcesados());
        assertEquals(0, resultado.getTotalGuardados());
        assertEquals(1, resultado.getTotalErrores());
        assertEquals("CLIENTE_NO_ENCONTRADO", resultado.getErrores().get(0).getTipoError());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarPedidoConZonaInexistente() {
        // Arrange
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("pedidos.csv");

        Pedido pedido = crearPedidoValido("P001", "CLI-123", "ZONA99", false);
        when(csvParser.parse(archivo)).thenReturn(List.of(pedido));

        when(pedidoRepository.existsByNumeroPedido(anyString())).thenReturn(false);
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(true);
        when(zonaRepository.findByCodigoZona("ZONA99")).thenReturn(Optional.empty());

        // Act
        ResultadoProcesamiento resultado = service.cargarPedidos(archivo);

        // Assert
        assertEquals(1, resultado.getTotalProcesados());
        assertEquals(0, resultado.getTotalGuardados());
        assertEquals(1, resultado.getTotalErrores());
        assertEquals("ZONA_NO_ENCONTRADA", resultado.getErrores().get(0).getTipoError());
    }

    @Test
    void debeRechazarPedidoConRefrigeracionEnZonaIncompatible() {
        // Arrange
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("pedidos.csv");

        Pedido pedido = crearPedidoValido("P001", "CLI-123", "ZONA3", true);
        when(csvParser.parse(archivo)).thenReturn(List.of(pedido));

        when(pedidoRepository.existsByNumeroPedido(anyString())).thenReturn(false);
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(true);
        when(zonaRepository.findByCodigoZona("ZONA3"))
                .thenReturn(Optional.of(crearZona("ZONA3", false)));

        // Act
        ResultadoProcesamiento resultado = service.cargarPedidos(archivo);

        // Assert
        assertEquals(1, resultado.getTotalProcesados());
        assertEquals(0, resultado.getTotalGuardados());
        assertEquals(1, resultado.getTotalErrores());
        assertEquals("ZONA_SIN_SOPORTE_REFRIGERACION", resultado.getErrores().get(0).getTipoError());
    }

    @Test
    void debeProcesarMultiplesPedidosConErroresMixtos() {
        // Arrange
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("pedidos.csv");

        Pedido pedidoValido = crearPedidoValido("P001", "CLI-123", "ZONA1", false);
        Pedido pedidoDuplicado = crearPedidoValido("P002", "CLI-123", "ZONA1", false);
        Pedido pedidoClienteInvalido = crearPedidoValido("P003", "CLI-999", "ZONA1", false);

        when(csvParser.parse(archivo))
                .thenReturn(Arrays.asList(pedidoValido, pedidoDuplicado, pedidoClienteInvalido));

        when(pedidoRepository.existsByNumeroPedido("P001")).thenReturn(false);
        when(pedidoRepository.existsByNumeroPedido("P002")).thenReturn(true);
        when(pedidoRepository.existsByNumeroPedido("P003")).thenReturn(false);

        when(clienteRepository.existsByClienteId("CLI-123")).thenReturn(true);
        when(clienteRepository.existsByClienteId("CLI-999")).thenReturn(false);

        when(zonaRepository.findByCodigoZona("ZONA1"))
                .thenReturn(Optional.of(crearZona("ZONA1", true)));

        when(pedidoRepository.save(any())).thenReturn(pedidoValido);

        // Act
        ResultadoProcesamiento resultado = service.cargarPedidos(archivo);

        // Assert
        assertEquals(3, resultado.getTotalProcesados());
        assertEquals(1, resultado.getTotalGuardados());
        assertEquals(2, resultado.getTotalErrores());

        // Verificar agrupación de errores
        assertTrue(resultado.getErroresAgrupados().containsKey("PEDIDO_DUPLICADO"));
        assertTrue(resultado.getErroresAgrupados().containsKey("CLIENTE_NO_ENCONTRADO"));
        assertEquals(1, resultado.getErroresAgrupados().get("PEDIDO_DUPLICADO"));
        assertEquals(1, resultado.getErroresAgrupados().get("CLIENTE_NO_ENCONTRADO"));
    }

    // Métodos auxiliares

    private Pedido crearPedidoValido(String numero, String clienteId, String zona, boolean refrigeracion) {
        return Pedido.builder()
                .numeroPedido(numero)
                .clienteId(clienteId)
                .fechaEntrega(LocalDate.now().plusDays(5))
                .estado(EstadoPedido.PENDIENTE)
                .zonaEntrega(zona)
                .requiereRefrigeracion(refrigeracion)
                .build();
    }

    private Zona crearZona(String codigo, boolean soporteRefrigeracion) {
        return Zona.builder()
                .codigoZona(codigo)
                .nombre("Zona " + codigo)
                .soporteRefrigeracion(soporteRefrigeracion)
                .activa(true)
                .build();
    }
}
