package com.pedidos.pedidos_service.infrastructure.adapter.output.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.pedidos.pedidos_service.application.dto.CsvRowDTO;
import com.pedidos.pedidos_service.application.ports.output.CsvParserPort;
import com.pedidos.pedidos_service.domain.exception.ArchivoInvalidoException;
import com.pedidos.pedidos_service.domain.model.EstadoPedido;
import com.pedidos.pedidos_service.domain.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CsvParserAdapter implements CsvParserPort {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public List<Pedido> parse(MultipartFile archivo) {
        log.info("Iniciando parseo del archivo CSV: {}", archivo.getOriginalFilename());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            // OpenCSV bean mapping con configuración optimizada
            CsvToBean<CsvRowDTO> csvToBean = new CsvToBeanBuilder<CsvRowDTO>(reader)
                    .withType(CsvRowDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            List<CsvRowDTO> csvRows = csvToBean.parse();
            List<Pedido> pedidos = new ArrayList<>(csvRows.size());

            // Convertir DTOs a modelos de dominio
            for (CsvRowDTO row : csvRows) {
                try {
                    Pedido pedido = convertirAPedido(row);
                    pedidos.add(pedido);
                } catch (Exception e) {
                    log.warn("Error parseando fila: {}, motivo: {}", row, e.getMessage());
                    // Crear un pedido inválido para que sea rechazado en validaciones
                    pedidos.add(crearPedidoInvalido(row));
                }
            }

            log.info("Parseo completado: {} registros procesados", pedidos.size());
            return pedidos;

        } catch (Exception e) {
            log.error("Error al procesar el archivo CSV", e);
            throw new ArchivoInvalidoException("Error al procesar el archivo CSV: " + e.getMessage(), e);
        }
    }

    /**
     * Convierte un DTO del CSV a modelo de dominio
     */
    private Pedido convertirAPedido(CsvRowDTO row) {
        return Pedido.builder()
                .numeroPedido(row.getNumeroPedido() != null ? row.getNumeroPedido().trim() : null)
                .clienteId(row.getClienteId() != null ? row.getClienteId().trim() : null)
                .fechaEntrega(parseFecha(row.getFechaEntrega()))
                .estado(parseEstado(row.getEstado()))
                .zonaEntrega(row.getZonaEntrega() != null ? row.getZonaEntrega().trim() : null)
                .requiereRefrigeracion(parseBoolean(row.getRequiereRefrigeracion()))
                .build();
    }

    /**
     * Crea un pedido marcado como inválido para manejo de errores de parseo
     */
    private Pedido crearPedidoInvalido(CsvRowDTO row) {
        return Pedido.builder()
                .numeroPedido(row.getNumeroPedido())
                .clienteId(row.getClienteId())
                .fechaEntrega(null) // Inválido intencionalmente
                .estado(null)
                .zonaEntrega(row.getZonaEntrega())
                .requiereRefrigeracion(false)
                .build();
    }

    /**
     * Parsea fecha en formato ISO (yyyy-MM-dd)
     */
    private LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(fecha.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("Fecha inválida: {}", fecha);
            return null;
        }
    }

    /**
     * Parsea estado del pedido
     */
    private EstadoPedido parseEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return null;
        }
        try {
            return EstadoPedido.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Estado inválido: {}", estado);
            return null;
        }
    }

    /**
     * Parsea booleano
     */
    private boolean parseBoolean(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return false;
        }
        String valorLower = valor.trim().toLowerCase();
        return "true".equals(valorLower) || "1".equals(valorLower) || "si".equals(valorLower);
    }
}
