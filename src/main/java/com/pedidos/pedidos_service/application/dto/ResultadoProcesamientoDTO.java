package com.pedidos.pedidos_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoProcesamientoDTO {
    private int totalProcesados;
    private int totalGuardados;
    private int totalErrores;

    @Builder.Default
    private List<ErrorValidacionDTO> errores = new ArrayList<>();

    @Builder.Default
    private Map<String, Integer> erroresAgrupados = new HashMap<>();
}
