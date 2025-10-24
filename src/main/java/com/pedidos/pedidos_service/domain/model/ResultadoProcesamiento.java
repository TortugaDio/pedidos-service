package com.pedidos.pedidos_service.domain.model;

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
public class ResultadoProcesamiento {
    private int totalProcesados;
    private int totalGuardados;
    private int totalErrores;

    @Builder.Default
    private List<ErrorValidacion> errores = new ArrayList<>();

    @Builder.Default
    private Map<String, Integer> erroresAgrupados = new HashMap<>();

    public void agregarError(ErrorValidacion error) {
        this.errores.add(error);
        this.erroresAgrupados.merge(error.getTipoError(), 1, Integer::sum);
        this.totalErrores++;
    }

    public void incrementarGuardados() {
        this.totalGuardados++;
    }
}
