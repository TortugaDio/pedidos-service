package com.pedidos.pedidos_service.application.ports.output;

import com.pedidos.pedidos_service.domain.model.Pedido;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvParserPort {
    List<Pedido> parse(MultipartFile archivo);
}
