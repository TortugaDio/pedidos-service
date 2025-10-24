package com.pedidos.pedidos_service.infrastructure.mapper;

import com.pedidos.pedidos_service.application.dto.ErrorValidacionDTO;
import com.pedidos.pedidos_service.application.dto.ResultadoProcesamientoDTO;
import com.pedidos.pedidos_service.domain.model.ErrorValidacion;
import com.pedidos.pedidos_service.domain.model.ResultadoProcesamiento;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResultadoProcesamientoMapper {

    public ResultadoProcesamientoDTO toDTO(ResultadoProcesamiento resultado) {
        if (resultado == null) {
            return null;
        }

        return ResultadoProcesamientoDTO.builder()
                .totalProcesados(resultado.getTotalProcesados())
                .totalGuardados(resultado.getTotalGuardados())
                .totalErrores(resultado.getTotalErrores())
                .errores(resultado.getErrores().stream()
                        .map(this::toErrorDTO)
                        .collect(Collectors.toList()))
                .erroresAgrupados(resultado.getErroresAgrupados())
                .build();
    }

    private ErrorValidacionDTO toErrorDTO(ErrorValidacion error) {
        return ErrorValidacionDTO.builder()
                .fila(error.getFila())
                .numeroPedido(error.getNumeroPedido())
                .tipoError(error.getTipoError())
                .mensaje(error.getMensaje())
                .build();
    }
}
