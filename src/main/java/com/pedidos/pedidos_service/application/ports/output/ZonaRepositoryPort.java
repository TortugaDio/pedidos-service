package com.pedidos.pedidos_service.application.ports.output;

import com.pedidos.pedidos_service.domain.model.Zona;

import java.util.Optional;

public interface ZonaRepositoryPort {
    Optional<Zona> findByCodigoZona(String codigoZona);
    boolean existsByCodigoZona(String codigoZona);
}
