package com.pedidos.pedidos_service.application.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvRowDTO {

    @CsvBindByName(column = "numeroPedido", required = true)
    private String numeroPedido;

    @CsvBindByName(column = "clienteId", required = true)
    private String clienteId;

    @CsvBindByName(column = "fechaEntrega", required = true)
    private String fechaEntrega;

    @CsvBindByName(column = "estado", required = true)
    private String estado;

    @CsvBindByName(column = "zonaEntrega", required = true)
    private String zonaEntrega;

    @CsvBindByName(column = "requiereRefrigeracion", required = true)
    private String requiereRefrigeracion;
}
