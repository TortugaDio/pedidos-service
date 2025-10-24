package com.pedidos.pedidos_service.infrastructure.adapter.input.rest;

import com.pedidos.pedidos_service.application.dto.ResultadoProcesamientoDTO;
import com.pedidos.pedidos_service.application.ports.input.CargarPedidosUseCase;
import com.pedidos.pedidos_service.domain.exception.ArchivoInvalidoException;
import com.pedidos.pedidos_service.domain.model.ResultadoProcesamiento;
import com.pedidos.pedidos_service.infrastructure.mapper.ResultadoProcesamientoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final CargarPedidosUseCase cargarPedidosUseCase;
    private final ResultadoProcesamientoMapper mapper;

    /**
     * Endpoint: POST /pedidos/cargar
     * Carga un archivo CSV con pedidos
     *
     * @param archivo Archivo CSV multipart
     * @return Resultado del procesamiento con estadísticas y errores
     */
    @PostMapping(value = "/cargar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultadoProcesamientoDTO> cargarPedidos(
            @RequestParam("archivo") MultipartFile archivo) {

        log.info("Recibida solicitud de carga de pedidos: {}", archivo.getOriginalFilename());

        try {
            ResultadoProcesamiento resultado = cargarPedidosUseCase.cargarPedidos(archivo);

            ResultadoProcesamientoDTO dto = mapper.toDTO(resultado);

            if (resultado.getTotalErrores() > 0) {
                // 207 Multi-Status: algunos registros OK, otros con errores
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(dto);
            } else {
                // 200 OK: todos los registros procesados exitosamente
                return ResponseEntity.ok(dto);
            }

        } catch (ArchivoInvalidoException e) {
            log.error("Archivo inválido: {}", e.getMessage());
            throw e; // Será manejado por @ControllerAdvice
        } catch (Exception e) {
            log.error("Error inesperado al cargar pedidos", e);
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
        }
    }
}
