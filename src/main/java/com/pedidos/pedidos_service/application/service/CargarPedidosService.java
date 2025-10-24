package com.pedidos.pedidos_service.application.service;

import com.pedidos.pedidos_service.application.ports.input.CargarPedidosUseCase;
import com.pedidos.pedidos_service.application.ports.output.ClienteRepositoryPort;
import com.pedidos.pedidos_service.application.ports.output.CsvParserPort;
import com.pedidos.pedidos_service.application.ports.output.PedidoRepositoryPort;
import com.pedidos.pedidos_service.application.ports.output.ZonaRepositoryPort;
import com.pedidos.pedidos_service.domain.exception.ArchivoInvalidoException;
import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.domain.model.ResultadoProcesamiento;
import com.pedidos.pedidos_service.domain.model.Zona;
import com.pedidos.pedidos_service.domain.validation.PedidoValidator;
import com.pedidos.pedidos_service.domain.validation.RefrigeracionZonaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargarPedidosService implements CargarPedidosUseCase {

    private final CsvParserPort csvParser;
    private final PedidoRepositoryPort pedidoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final ZonaRepositoryPort zonaRepository;
    private final List<PedidoValidator> validators;
    private final RefrigeracionZonaValidator refrigeracionValidator;

    @Override
    @Transactional
    public ResultadoProcesamiento cargarPedidos(MultipartFile archivo) {
        log.info("Iniciando carga de pedidos desde archivo: {}", archivo.getOriginalFilename());

        validarArchivo(archivo);

        List<Pedido> pedidos = csvParser.parse(archivo);

        ResultadoProcesamiento resultado = ResultadoProcesamiento.builder()
                .totalProcesados(pedidos.size())
                .build();

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido pedido = pedidos.get(i);
            int fila = i + 2;

            Optional<ErrorValidacion> error = validarPedido(pedido, fila);

            if (error.isPresent()) {
                resultado.agregarError(error.get());
            } else {
                pedidoRepository.save(pedido);
                resultado.incrementarGuardados();
            }
        }

        log.info("Procesamiento completado: {} procesados, {} guardados, {} errores",
                resultado.getTotalProcesados(),
                resultado.getTotalGuardados(),
                resultado.getTotalErrores());

        return resultado;
    }

    private Optional<ErrorValidacion> validarPedido(Pedido pedido, int fila) {

        for (PedidoValidator validator : validators) {
            Optional<ErrorValidacion> error = validator.validate(pedido, fila);
            if (error.isPresent()) {
                return error;
            }
        }

        if (pedidoRepository.existsByNumeroPedido(pedido.getNumeroPedido())) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError("PEDIDO_DUPLICADO")
                    .mensaje("El número de pedido ya existe en la base de datos")
                    .build());
        }

        if (!clienteRepository.existsByClienteId(pedido.getClienteId())) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError("CLIENTE_NO_ENCONTRADO")
                    .mensaje("El cliente no existe: " + pedido.getClienteId())
                    .build());
        }

        Optional<Zona> zonaOpt = zonaRepository.findByCodigoZona(pedido.getZonaEntrega());
        if (zonaOpt.isEmpty()) {
            return Optional.of(ErrorValidacion.builder()
                    .fila(fila)
                    .numeroPedido(pedido.getNumeroPedido())
                    .tipoError("ZONA_NO_ENCONTRADA")
                    .mensaje("La zona no existe: " + pedido.getZonaEntrega())
                    .build());
        }

        Optional<ErrorValidacion> errorRefrigeracion =
                refrigeracionValidator.validate(pedido, zonaOpt.get(), fila);
        if (errorRefrigeracion.isPresent()) {
            return errorRefrigeracion;
        }

        return Optional.empty();
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoInvalidoException("El archivo está vacío");
        }

        String filename = archivo.getOriginalFilename();
        if (filename == null || !filename.endsWith(".csv")) {
            throw new ArchivoInvalidoException("El archivo debe ser de tipo CSV");
        }
    }
}
