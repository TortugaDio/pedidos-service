package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository;

import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ZonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZonaJpaRepository extends JpaRepository<ZonaEntity, String> {
    Optional<ZonaEntity> findByCodigoZona(String codigoZona);
    boolean existsByCodigoZona(String codigoZona);
}
