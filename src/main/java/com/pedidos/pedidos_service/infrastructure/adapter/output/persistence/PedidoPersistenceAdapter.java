package com.pedidos.pedidos_service.infrastructure.adapter.output.persistence;

import com.pedidos.pedidos_service.application.ports.output.PedidoRepositoryPort;
import com.pedidos.pedidos_service.domain.model.Pedido;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.PedidoEntity;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.PedidoJpaRepository;
import com.pedidos.pedidos_service.infrastructure.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PedidoPersistenceAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository jpaRepository;
    private final PedidoMapper mapper;

    @Override
    public Pedido save(Pedido pedido) {
        PedidoEntity entity = mapper.toEntity(pedido);
        PedidoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Pedido> findByNumeroPedido(String numeroPedido) {
        return jpaRepository.findByNumeroPedido(numeroPedido)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNumeroPedido(String numeroPedido) {
        return jpaRepository.existsByNumeroPedido(numeroPedido);
    }
}
