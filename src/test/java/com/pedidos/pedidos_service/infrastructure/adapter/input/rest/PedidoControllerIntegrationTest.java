package com.pedidos.pedidos_service.infrastructure.adapter.input.rest;

import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ClienteJpaRepository;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.PedidoJpaRepository;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ZonaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoJpaRepository pedidoRepository;

    @Autowired
    private ClienteJpaRepository clienteRepository;

    @Autowired
    private ZonaJpaRepository zonaRepository;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
    }

    @Test
    void debeCargarPedidosValidosCorrectamente() throws Exception {
        // Arrange
        String csvContent = """
                numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion
                P001,CLI-123,2025-12-10,PENDIENTE,ZONA1,true
                P002,CLI-456,2025-12-15,CONFIRMADO,ZONA2,false
                """;

        MockMultipartFile file = new MockMultipartFile(
                "archivo",
                "pedidos.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/pedidos/cargar")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProcesados").value(2))
                .andExpect(jsonPath("$.totalGuardados").value(2))
                .andExpect(jsonPath("$.totalErrores").value(0))
                .andExpect(jsonPath("$.errores").isEmpty());

        // Verificar persistencia
        assertEquals(2, pedidoRepository.count());
    }

    @Test
    void debeRetornarErroresParaPedidosInvalidos() throws Exception {
        // Arrange - CSV con múltiples errores
        String csvContent = """
                numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion
                P001,CLI-999,2025-12-10,PENDIENTE,ZONA1,false
                P002,CLI-123,2020-01-01,PENDIENTE,ZONA1,false
                P003,CLI-123,2025-12-10,INVALIDO,ZONA1,false
                P004,CLI-123,2025-12-10,PENDIENTE,ZONA99,false
                P005,CLI-123,2025-12-10,PENDIENTE,ZONA3,true
                """;

        MockMultipartFile file = new MockMultipartFile(
                "archivo",
                "pedidos.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/pedidos/cargar")
                        .file(file))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$.totalProcesados").value(5))
                .andExpect(jsonPath("$.totalGuardados").value(0))
                .andExpect(jsonPath("$.totalErrores").value(5))
                .andExpect(jsonPath("$.errores", hasSize(5)))
                .andExpect(jsonPath("$.erroresAgrupados.CLIENTE_NO_ENCONTRADO").value(1))
                .andExpect(jsonPath("$.erroresAgrupados.FECHA_ENTREGA_INVALIDA").value(1))
                .andExpect(jsonPath("$.erroresAgrupados.ESTADO_INVALIDO").value(1))
                .andExpect(jsonPath("$.erroresAgrupados.ZONA_NO_ENCONTRADA").value(1))
                .andExpect(jsonPath("$.erroresAgrupados.ZONA_SIN_SOPORTE_REFRIGERACION").value(1));

        // Verificar que no se guardó nada
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    void debeProcesarPedidosMixtosCorrectamente() throws Exception {
        // Arrange - Algunos válidos, algunos inválidos
        String csvContent = """
                numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion
                P001,CLI-123,2025-12-10,PENDIENTE,ZONA1,true
                P002,CLI-999,2025-12-10,PENDIENTE,ZONA1,false
                P003,CLI-456,2025-12-15,ENTREGADO,ZONA5,false
                """;

        MockMultipartFile file = new MockMultipartFile(
                "archivo",
                "pedidos.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/pedidos/cargar")
                        .file(file))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$.totalProcesados").value(3))
                .andExpect(jsonPath("$.totalGuardados").value(2))
                .andExpect(jsonPath("$.totalErrores").value(1))
                .andExpect(jsonPath("$.errores[0].tipoError").value("CLIENTE_NO_ENCONTRADO"))
                .andExpect(jsonPath("$.errores[0].fila").value(3));

        // Verificar persistencia
        assertEquals(2, pedidoRepository.count());
        assertTrue(pedidoRepository.existsByNumeroPedido("P001"));
        assertTrue(pedidoRepository.existsByNumeroPedido("P003"));
        assertFalse(pedidoRepository.existsByNumeroPedido("P002"));
    }

    @Test
    void debeRechazarArchivoVacio() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "archivo",
                "vacio.csv",
                "text/csv",
                new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/pedidos/cargar")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Archivo inválido"));
    }

    @Test
    void debeRechazarArchivoNoCSV() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "archivo",
                "documento.txt",
                "text/plain",
                "contenido".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/pedidos/cargar")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(containsString("CSV")));
    }
}
