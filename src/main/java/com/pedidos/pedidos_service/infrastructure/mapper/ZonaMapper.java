package com.pedidos.pedidos_service.infrastructure.mapper;

import com.pedidos.pedidos_service.domain.model.Zona;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ZonaEntity;
import org.springframework.stereotype.Component;

@Component
public class ZonaMapper {

    public ZonaEntity toEntity(Zona zona) {
        if (zona == null) {
            return null;
        }

        return ZonaEntity.builder()
                .codigoZona(zona.getCodigoZona())
                .nombre(zona.getNombre())
                .soporteRefrigeracion(zona.isSoporteRefrigeracion())
                .activa(zona.isActiva())
                .build();
    }

    public Zona toDomain(ZonaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Zona.builder()
                .codigoZona(entity.getCodigoZona())
                .nombre(entity.getNombre())
                .soporteRefrigeracion(entity.isSoporteRefrigeracion())
                .activa(entity.isActiva())
                .build();
    }
}
