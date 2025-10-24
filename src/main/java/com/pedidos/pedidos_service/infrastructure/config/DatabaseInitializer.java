package com.pedidos.pedidos_service.infrastructure.config;

import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.entity.ZonaEntity;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ClienteJpaRepository;
import com.pedidos.pedidos_service.infrastructure.adapter.output.persistence.repository.ZonaJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final ClienteJpaRepository clienteRepository;
    private final ZonaJpaRepository zonaRepository;

    @Override
    public void run(String... args) {
        log.info("Inicializando base de datos con datos de prueba...");
        //COMENTADO POR USO DE SCHEMA.SQL EN RESOURCES
        /*// Crear clientes de prueba
        ClienteEntity cliente1 = ClienteEntity.builder()
                .clienteId("CLI-123")
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .activo(true)
                .build();

        ClienteEntity cliente2 = ClienteEntity.builder()
                .clienteId("CLI-456")
                .nombre("María García")
                .email("maria.garcia@example.com")
                .activo(true)
                .build();

        ClienteEntity cliente3 = ClienteEntity.builder()
                .clienteId("CLI-789")
                .nombre("Carlos López")
                .email("carlos.lopez@example.com")
                .activo(true)
                .build();

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));

        // Crear zonas de prueba
        ZonaEntity zona1 = ZonaEntity.builder()
                .codigoZona("ZONA1")
                .nombre("Zona Norte")
                .soporteRefrigeracion(true)
                .activa(true)
                .build();

        ZonaEntity zona2 = ZonaEntity.builder()
                .codigoZona("ZONA2")
                .nombre("Zona Sur")
                .soporteRefrigeracion(true)
                .activa(true)
                .build();

        ZonaEntity zona3 = ZonaEntity.builder()
                .codigoZona("ZONA3")
                .nombre("Zona Centro")
                .soporteRefrigeracion(false)
                .activa(true)
                .build();

        ZonaEntity zona4 = ZonaEntity.builder()
                .codigoZona("ZONA4")
                .nombre("Zona Este")
                .soporteRefrigeracion(true)
                .activa(true)
                .build();

        ZonaEntity zona5 = ZonaEntity.builder()
                .codigoZona("ZONA5")
                .nombre("Zona Oeste")
                .soporteRefrigeracion(false)
                .activa(true)
                .build();

        zonaRepository.saveAll(Arrays.asList(zona1, zona2, zona3, zona4, zona5));

        log.info("Base de datos inicializada con {} clientes y {} zonas",
                clienteRepository.count(),
                zonaRepository.count());
    */
    }
}
