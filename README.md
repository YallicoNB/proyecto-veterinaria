# Proyecto Veterinaria - Clínica Veterinaria Multi-Servicio

Este es un proyecto académico de una **clínica veterinaria** desarrollado con **Spring Boot** y **Java**. El sistema ofrece múltiples servicios:

- **Usuarios**: Registro y login con roles
- **Veterinaria**: Consultas, vacunas e historias clínicas
- **Lavandería**: Servicio de baño y limpieza para mascotas
- **Adopción**: Gestión de mascotas en adopción
- **Tienda**: Inventario y ventas de productos para mascotas

---

## Tecnologías Usadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 17+ | Lenguaje de programación |
| Spring Boot | 3.2.0 | Framework principal |
| Maven | 3.9+ | Gestión de dependencias |
| H2 Database | - | Base de datos en memoria (desarrollo) |
| Spring Security | - | Seguridad (configurada como permitAll) |
| JWT | 0.12.3 | Tokens de autenticación |
| JPA/Hibernate | - | ORM para base de datos |

---

## Estructura del Proyecto

```
proyecto-veterinaria/
├── pom.xml
└── src/main/
    ├── java/com/veterinaria/
    │   ├── ProyectoVeterinariaApplication.java
    │   ├── model/           (Entidades)
    │   ├── repository/      (Acceso a datos)
    │   ├── service/         (Lógica de negocio)
    │   ├── controller/      (Endpoints API)
    │   └── config/          (Configuración)
    └── resources/
        └── application.yml  (Configuración)
```

---

## Requisitos para ejecutar

- Java JDK 17 o superior
- Maven 3.9 o superior

---

## Cómo empezar

### 1. Clonar el proyecto
```bash
git clone https://github.com/YallicoNB/proyecto-veterinaria.git
cd proyecto-veterinaria
```

### 2. Instalar dependencias
```bash
mvn clean install -DskipTests
```

### 3. Ejecutar el proyecto
```bash
mvn spring-boot:run
```

El proyecto estará disponible en: **http://localhost:8080**

### 4. Endpoints disponibles actualmente

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/usuarios` | Listar todos los usuarios |
| GET | `/api/usuarios/{id}` | Buscar usuario por ID |
| GET | `/api/usuarios/buscar?rol=ADMIN` | Filtrar usuarios por rol |
| POST | `/api/usuarios/registro` | Crear nuevo usuario |
| POST | `/api/usuarios/login` | Iniciar sesión |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

---

## Ramas y Contribución

### Para contribuir:

```bash
# 1. Actualizar tu repositorio local
git pull origin master

# 2. Hacer cambios en tu código

# 3. Guardar cambios
git add .
git commit -m "Descripción del cambio"

# 4. Subir al repositorio
git push origin master
```

### Estructura de ramas:
- **master**: Rama principal (solo código funcionando)
- Cada integrante trabaja en su propia funcionalidad

---

## Plan de Desarrollo - 5 Pasos

### Paso 1: ✅ COMPLETADO - Módulo de Usuarios
- Modelos: Usuario, Mascota, Rol, Enums
- Repositorios: UsuarioRepository, MascotaRepository
- Servicios: UsuarioService
- Controladores: UsuarioController
- Endpoints: CRUD completo de usuarios + login

---

### Paso 2: EN PROGRESO - Veterinaria (Consultas, Vacunas, Historia Clínica)

**Modelos:**
```java
// HistoriaClinica.java
- Long id
- Long idMascota (relación con Mascota)
- String motivoConsulta
- String diagnostico
- String tratamiento
- LocalDateTime fechaCreacion

// Consulta.java
- Long id
- Long idMascota
- Long idVeterinario
- String sintomas
- String diagnostico
- String receta
- LocalDateTime fecha
- String estado (PENDIENTE, REALIZADA, CANCELADA)

// Vacuna.java
- Long id
- Long idMascota
- String nombreVacuna
- LocalDate fechaAplicacion
- LocalDate fechaProxima
- String lote
```

**Repositorios:**
- HistoriaClinicaRepository.java - findByIdMascota, save, delete
- ConsultaRepository.java - findByIdMascota, findByEstado, findByVeterinario
- VacunaRepository.java - findByIdMascota, findByFechaProxima

**Servicios:**
- HistoriaClinicaService.java - crear, buscar por mascota, listar todas
- ConsultaService.java - agendar, atender, cancelar, historial
- VacunaService.java - registrarVacuna, proximasVacunas

**Endpoints:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/veterinaria/historia/{idMascota}` | Ver historia clínica |
| POST | `/api/veterinaria/consulta` | Crear consulta |
| GET | `/api/veterinaria/consulta/mascota/{id}` | Ver consultas de mascota |
| PUT | `/api/veterinaria/consulta/{id}/atender` | Atender consulta |
| POST | `/api/veterinaria/vacuna` | Registrar vacuna |
| GET | `/api/veterinaria/vacuna/mascota/{id}` | Ver vacunas de mascota |

---

### Paso 3: PENDIENTE - Lavandería (Servicios de baño para mascotas)

**Modelos:**
```java
// ServicioLavado.java
- Long id
- Long idMascota
- String tipoServicio (BAÑO, CORTE, SPA, LIMPIEZA)
- Double precio
- LocalDateTime fechaHora
- String estado (PENDIENTE, EN_PROCESO, TERMINADO, ENTREGADO)
- String observaciones

// TipoServicio.java (enum)
- BAÑO, CORTE, SPA, LIMPIEZA, PELUQUERIA
```

**Repositorios:**
- ServicioLavadoRepository.java - findByEstado, findByFechaHoraBetween

**Servicios:**
- LavanderiaService.java - crearServicio, cambiarEstado, listarPendientes

**Endpoints:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/lavanderia/servicio` | Crear servicio de lavado |
| GET | `/api/lavanderia/servicio` | Listar todos los servicios |
| GET | `/api/lavanderia/servicio/{id}` | Ver servicio específico |
| PUT | `/api/lavanderia/servicio/{id}/estado` | Cambiar estado |
| GET | `/api/lavanderia/servicio/pendientes` | Ver pendientes |

---

### Paso 4: PENDIENTE - Adopción (Solicitudes de adopción, mascotas disponibles)

**Modelos:**
```java
// MascotaAdoptable.java
- Long id
- String nombre
- TipoMascota tipo
- String raza
- Integer edad
- Sexo sexo
- String descripcion
- String fotoUrl
- LocalDateTime fechaRegistro
- Boolean disponible (true/false)

// SolicitudAdopcion.java
- Long id
- Long idMascota
- Long idSolicitante (Usuario)
- String nombreSolicitante
- String email
- String telefono
- String direccion
- String motivo
- String estado (PENDIENTE, APROBADA, RECHAZADA)
- LocalDateTime fechaSolicitud
- String observaciones
```

**Repositorios:**
- MascotaAdoptableRepository.java - findByDisponible, findByTipo
- SolicitudAdopcionRepository.java - findByEstado, findByIdMascota

**Servicios:**
- AdopcionService.java - registrarMascota, listarDisponibles, crearSolicitud, aprobarSolicitud

**Endpoints:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/adopcion/mascotas` | Listar mascotas disponibles |
| POST | `/api/adopcion/mascotas` | Agregar mascota en adopción |
| GET | `/api/adopcion/mascotas/{id}` | Ver detalles de mascota |
| POST | `/api/adopcion/solicitud` | Crear solicitud de adopción |
| GET | `/api/adopcion/solicitud` | Listar solicitudes |
| PUT | `/api/adopcion/solicitud/{id}/aprobar` | Aprobar solicitud |
| PUT | `/api/adopcion/solicitud/{id}/rechazar` | Rechazar solicitud |

---

### Paso 5: PENDIENTE - Tienda (Productos, ventas, inventario)

**Modelos:**
```java
// Categoria.java
- Long id
- String nombre
- String descripcion

// Producto.java
- Long id
- String nombre
- String descripcion
- Double precio
- Integer stock
- Long idCategoria
- String imagenUrl
- Boolean activo

// Venta.java
- Long id
- Long idUsuario (vendedor)
- LocalDateTime fecha
- Double total
- String estado (PENDIENTE, COMPLETADA, CANCELADA)

// DetalleVenta.java
- Long id
- Long idVenta
- Long idProducto
- Integer cantidad
- Double precioUnitario
```

**Repositorios:**
- CategoriaRepository.java
- ProductoRepository.java - findByStockLessThan, findByCategoria
- VentaRepository.java
- DetalleVentaRepository.java

**Servicios:**
- ProductoService.java - CRUD productos, actualizarStock
- VentaService.java - crearVenta, completarVenta, historial

**Endpoints:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tienda/productos` | Listar productos |
| POST | `/api/tienda/productos` | Crear producto |
| GET | `/api/tienda/productos/{id}` | Ver producto |
| PUT | `/api/tienda/productos/{id}` | Actualizar producto |
| POST | `/api/tienda/venta` | Crear venta |
| GET | `/api/tienda/venta` | Listar ventas |
| GET | `/api/tienda/productos/bajo-stock` | Productos con poco stock |

---

## Orden de Desarrollo (Modelo → Repositorio → Service → Controller)

Para cada paso, el orden de implementación es:

1. **Modelos/Entidades** - Definir las clases de la base de datos
2. **Repositorios** - Crear la interfaz de acceso a datos
3. **Servicios** - Implementar la lógica de negocio
4. **Controladores** - Crear los endpoints REST

---

## Equipo de Desarrollo

| Integrante | Rol | Módulo |
|------------|-----|--------|
| - | Desarrollador | Módulo de Usuarios (Completado) |
| - | Desarrollador | Veterinaria (En progreso) |
| - | Desarrollador | Lavandería (Pendiente) |
| - | Desarrollador | Adopción (Pendiente) |
| - | Desarrollador | Tienda (Pendiente) |

---

**Proyecto desarrollado por el equipo de Desarrollo Web Integrado**

**Repositorio GitHub:** https://github.com/YallicoNB/proyecto-veterinaria