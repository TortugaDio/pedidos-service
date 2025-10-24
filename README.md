# 🚀 Microservicio de Carga de Pedidos

Microservicio desarrollado con **Spring Boot 3** y **Java 17** que permite cargar archivos CSV con pedidos de envío, aplicando validaciones de negocio complejas y procesamiento eficiente utilizando **Arquitectura Hexagonal**.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Instalación](#instalación)
- [Uso](#uso)
- [Validaciones Implementadas](#validaciones-implementadas)
- [Testing](#testing)
- [Decisiones de Diseño](#decisiones-de-diseño)

---

## ✨ Características

- ✅ **Arquitectura Hexagonal** (Puertos y Adaptadores)
- ✅ **Principios SOLID** aplicados en todo el código
- ✅ **Clean Code** y separación de responsabilidades
- ✅ **Validaciones de negocio independientes y extensibles**
- ✅ **Procesamiento eficiente de archivos CSV**
- ✅ **Agrupación automática de errores por tipo**
- ✅ **Cobertura de tests unitarios e integración**
- ✅ **Manejo robusto de errores**
- ✅ **Base de datos H2 en memoria con datos de prueba**

---

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────┐
│                   DOMINIO                       │
│  ┌──────────────────────────────────────────┐  │
│  │  Entidades: Pedido, Cliente, Zona        │  │
│  │  Value Objects: EstadoPedido, Error      │  │
│  │  Servicios de Dominio: Validadores       │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
                      ▲
                      │
┌─────────────────────────────────────────────────┐
│                APLICACIÓN                       │
│  ┌──────────────────────────────────────────┐  │
│  │  Puertos (Interfaces):                   │  │
│  │    - Input: CargarPedidosUseCase         │  │
│  │    - Output: Repositories, CsvParser     │  │
│  │  Servicios de Aplicación                 │  │
│  │  DTOs                                     │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
                      ▲
                      │
┌─────────────────────────────────────────────────┐
│             INFRAESTRUCTURA                     │
│  ┌──────────────────────────────────────────┐  │
│  │  Adaptadores:                            │  │
│  │    - REST Controller                     │  │
│  │    - JPA Repositories                    │  │
│  │    - CSV Parser                          │  │
│  │  Entidades JPA                           │  │
│  │  Mappers                                 │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

### Principios SOLID Aplicados

1. **Single Responsibility Principle (SRP)**: Cada validador tiene una única responsabilidad
2. **Open/Closed Principle (OCP)**: Sistema extensible mediante validadores sin modificar código existente
3. **Liskov Substitution Principle (LSP)**: Las interfaces permiten sustituir implementaciones
4. **Interface Segregation Principle (ISP)**: Puertos específicos y cohesivos
5. **Dependency Inversion Principle (DIP)**: Dependencias hacia abstracciones (puertos), no implementaciones

---

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **H2 Database** (en memoria)
- **OpenCSV** para procesamiento de CSV
- **Lombok** para reducir boilerplate
- **JUnit 5** y **Mockito** para testing
- **Maven** como gestor de dependencias

---

## 📁 Estructura del Proyecto

```
pedidos-service/
├── src/main/java/com/pedidos/
│   ├── domain/                      # Capa de Dominio
│   │   ├── model/                   # Entidades y Value Objects
│   │   │   ├── Pedido.java
│   │   │   ├── Cliente.java
│   │   │   ├── Zona.java
│   │   │   ├── EstadoPedido.java
│   │   │   ├── ErrorValidacion.java
│   │   │   └── ResultadoProcesamiento.java
│   │   ├── validation/              # Validadores de Dominio
│   │   │   ├── PedidoValidator.java
│   │   │   ├── NumeroPedidoValidator.java
│   │   │   ├── FechaEntregaValidator.java
│   │   │   ├── EstadoPedidoValidator.java
│   │   │   └── RefrigeracionZonaValidator.java
│   │   └── exception/               # Excepciones de Dominio
│   │       └── DomainException.java
│   │
│   ├── application/                 # Capa de Aplicación
│   │   ├── ports/
│   │   │   ├── input/              # Casos de Uso (Interfaces)
│   │   │   │   └── CargarPedidosUseCase.java
│   │   │   └── output/             # Puertos de Salida (Interfaces)
│   │   │       ├── PedidoRepositoryPort.java
│   │   │       ├── ClienteRepositoryPort.java
│   │   │       ├── ZonaRepositoryPort.java
│   │   │       └── CsvParserPort.java
│   │   ├── service/                # Servicios de Aplicación
│   │   │   └── CargarPedidosService.java
│   │   └── dto/                    # DTOs
│   │       ├── CsvRowDTO.java
│   │       ├── ErrorValidacionDTO.java
│   │       └── ResultadoProcesamientoDTO.java
│   │
│   └── infrastructure/              # Capa de Infraestructura
│       ├── adapter/
│       │   ├── input/
│       │   │   └── rest/           # Controladores REST
│       │   │       ├── PedidoController.java
│       │   │       └── GlobalExceptionHandler.java
│       │   └── output/
│       │       ├── persistence/    # Adaptadores JPA
│       │       │   ├── entity/     # Entidades JPA
│       │       │   ├── repository/ # Repositorios JPA
│       │       │   └── *PersistenceAdapter.java
│       │       └── csv/
│       │           └── CsvParserAdapter.java
│       ├── mapper/                 # Mappers entre capas
│       │   ├── PedidoMapper.java
│       │   └── ResultadoProcesamientoMapper.java
│       └── config/                 # Configuraciones
│           └── DatabaseInitializer.java
│
└── src/test/java/                  # Tests
    ├── domain/validation/          # Tests Unitarios
    └── application/service/        # Tests de Integración
```

---

## 🚀 Instalación

### Prerrequisitos

- Java 17 o superior
- Maven 3.8+

### Pasos

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/pedidos-service.git
   cd pedidos-service
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **La aplicación estará disponible en:**
    - API: `http://localhost:8080/api`
    - H2 Console: `http://localhost:8080/api/h2-console`

---

## 📝 Uso

### Endpoint Principal

**POST** `/api/pedidos/cargar`

Carga un archivo CSV con pedidos de envío.

#### Request

```bash
curl -X POST http://localhost:8080/api/pedidos/cargar \
  -F "archivo=@pedidos.csv"
```

#### Formato del CSV

```csv
numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion
P001,CLI-123,2025-08-10,PENDIENTE,ZONA1,true
P002,CLI-456,2025-08-12,ENTREGADO,ZONA5,false
```

#### Response (200 OK - Todos válidos)

```json
{
  "totalProcesados": 2,
  "totalGuardados": 2,
  "totalErrores": 0,
  "errores": [],
  "erroresAgrupados": {}
}
```

#### Response (207 Multi-Status - Con errores)

```json
{
  "totalProcesados": 5,
  "totalGuardados": 2,
  "totalErrores": 3,
  "errores": [
    {
      "fila": 2,
      "numeroPedido": "P002",
      "tipoError": "CLIENTE_NO_ENCONTRADO",
      "mensaje": "El cliente no existe: CLI-999"
    },
    {
      "fila": 3,
      "numeroPedido": "P003",
      "tipoError": "FECHA_ENTREGA_INVALIDA",
      "mensaje": "La fecha de entrega no puede ser una fecha pasada"
    },
    {
      "fila": 5,
      "numeroPedido": "P005",
      "tipoError": "ZONA_SIN_SOPORTE_REFRIGERACION",
      "mensaje": "La zona ZONA3 no soporta refrigeración requerida por el pedido"
    }
  ],
  "erroresAgrupados": {
    "CLIENTE_NO_ENCONTRADO": 1,
    "FECHA_ENTREGA_INVALIDA": 1,
    "ZONA_SIN_SOPORTE_REFRIGERACION": 1
  }
}
```

---

## ✅ Validaciones Implementadas

### 1. Número de Pedido
- ✅ Debe ser alfanumérico
- ✅ No puede estar vacío
- ✅ Debe ser único en la base de datos

### 2. Cliente
- ✅ Debe existir en la tabla `Clientes`

### 3. Fecha de Entrega
- ✅ No puede ser una fecha pasada
- ✅ Debe ser una fecha válida (formato ISO: yyyy-MM-dd)

### 4. Estado
- ✅ Debe ser uno de: `PENDIENTE`, `CONFIRMADO`, `ENTREGADO`

### 5. Zona de Entrega
- ✅ Debe existir en la tabla `Zonas`
- ✅ Si el pedido requiere refrigeración, la zona debe soportarla

### Datos de Prueba Precargados

**Clientes:**
- CLI-123 (Juan Pérez)
- CLI-456 (María García)
- CLI-789 (Carlos López)

**Zonas:**
- ZONA1 (Con refrigeración)
- ZONA2 (Con refrigeración)
- ZONA3 (Sin refrigeración)
- ZONA4 (Con refrigeración)
- ZONA5 (Sin refrigeración)

---

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn test
```

### Cobertura de Tests

- **Tests Unitarios**: Validadores de dominio
- **Tests de Integración**: Servicio de aplicación con mocks
- **Tests End-to-End**: Controlador REST completo

### Ejemplo de Test

```java
@Test
void debeRechazarPedidoConFechaPasada() {
    Pedido pedido = Pedido.builder()
            .numeroPedido("P001")
            .fechaEntrega(LocalDate.now().minusDays(1))
            .build();
    
    Optional<ErrorValidacion> resultado = validator.validate(pedido, 1);
    
    assertTrue(resultado.isPresent());
    assertEquals("FECHA_ENTREGA_INVALIDA", resultado.get().getTipoError());
}
```

---

## 💡 Decisiones de Diseño

### Procesamiento Eficiente del CSV

1. **Lectura con BufferedReader**: Optimiza I/O del archivo
2. **OpenCSV con Bean Mapping**: Mapeo automático y eficiente de columnas
3. **Validaciones en orden**: Primero las que no requieren I/O, luego las que sí
4. **Fail-Fast**: Detiene validación al primer error encontrado por registro
5. **Transacción única**: Persiste todos los pedidos válidos en una sola transacción

### Para Volúmenes Mayores (>100k registros)

Si se necesitara procesar archivos muy grandes, se implementaría:
- **Procesamiento en chunks** con `CompletableFuture`
- **Batch inserts** configurables en JPA
- **Parallel streams** para validaciones
- **Pagination** en respuesta de errores

### Separación de Capas

- **Dominio**: Lógica de negocio pura, sin dependencias de frameworks
- **Aplicación**: Orquestación de casos de uso
- **Infraestructura**: Detalles técnicos (JPA, REST, CSV)

### Extensibilidad

Agregar nuevas validaciones es simple:

```java
@Component
public class NuevaValidacion implements PedidoValidator {
    @Override
    public Optional<ErrorValidacion> validate(Pedido pedido, int fila) {
        // Lógica de validación
    }
}
```

Spring Boot lo detectará automáticamente y lo incluirá en la cadena de validaciones.

---

## 📄 Licencia

Este proyecto es un ejercicio técnico de demostración.

---

## 👤 Leonardo Condori Ancco

Desarrollado como prueba técnica para demostrar conocimientos en:
- Arquitectura Hexagonal
- Principios SOLID
- Clean Code
- Spring Boot y Microservicios
- Testing exhaustivo