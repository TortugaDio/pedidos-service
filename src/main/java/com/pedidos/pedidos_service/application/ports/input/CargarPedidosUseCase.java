package com.pedidos.pedidos_service.application.ports.input;

import com.pedidos.pedidos_service.domain.model.ResultadoProcesamiento;
import org.springframework.web.multipart.MultipartFile;

public interface CargarPedidosUseCase {
    ResultadoProcesamiento cargarPedidos(MultipartFile archivo);
}
