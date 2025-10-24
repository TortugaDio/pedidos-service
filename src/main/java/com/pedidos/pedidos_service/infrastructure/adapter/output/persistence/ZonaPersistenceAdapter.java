package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence;

import com.pedidos.pedidos_service.application.ports.output.ZonaRepositoryPort;
import com.pedidos.pedidos_service.domain.model.Zona;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ZonaJpaRepository;
import com.pedidos.pedidos_service.infrastructure.mapper.ZonaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ZonaPersistenceAdapter implements ZonaRepositoryPort {

    private final ZonaJpaRepository jpaRepository;
    private final ZonaMapper mapper;

    @Override
    public Optional<Zona> findByCodigoZona(String codigoZona) {
        return jpaRepository.findByCodigoZona(codigoZona)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoZona(String codigoZona) {
        return jpaRepository.existsByCodigoZona(codigoZona);
    }
}
