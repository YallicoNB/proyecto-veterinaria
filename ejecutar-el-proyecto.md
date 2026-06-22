# Ejecutar el Proyecto

## Requisitos

- Java 17+
- Maven 3.8+
- MySQL 8+ corriendo en `localhost:3306`

## Configurar base de datos

La aplicación usa MySQL con las siguientes credenciales (archivo `application.properties`):

```
url: jdbc:mysql://localhost:3306/veterinaria_db
usuario: root
password: root
```

Si tu MySQL tiene otra contraseña, cámbiala en `src/main/resources/application.properties` línea 12.

La BD y las tablas se crean automáticamente al iniciar (`createDatabaseIfNotExist=true`, `ddl-auto=update`).

## Ejecutar

```bash
mvn spring-boot:run
```

La app arranca en `http://localhost:8080`.

Al iniciar, el `DataLoader` carga datos de prueba automáticamente:

### Usuarios precargados

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | ADMIN |
| `veterinario` | `vet123` | VETERINARIO |
| `cliente` | `cliente123` | CLIENTE_TIENDA |
| `empleado` | `empleado123` | EMPLEADO_LAVANDERIA |

### Otros datos precargados

- 10 productos en la tienda
- 6 mascotas para consultas veterinarias
- 3 mascotas disponibles para adopción
- Servicios de lavandería, consultas, vacunas e historias clínicas

---

# Pruebas en Postman

## 1. Obtener token de autenticación

```
POST http://localhost:8080/api/usuarios/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta exitosa (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "rol": "ADMIN"
}
```

Para los endpoints protegidos, copia el `token` y úsalo en el header `Authorization: Bearer <token>`.

---

## 2. Endpoints públicos (no requieren token)

### Listar productos
```
GET http://localhost:8080/api/tienda/productos
```

### Obtener producto por ID
```
GET http://localhost:8080/api/tienda/productos/1
```

### Listar mascotas disponibles para adopción
```
GET http://localhost:8080/api/adopcion/disponibles
```

---

## 3. Endpoints protegidos por rol

Usar header: `Authorization: Bearer <token>`

### Usuarios (solo ADMIN)

```
GET http://localhost:8080/api/usuarios
GET http://localhost:8080/api/usuarios/1
GET http://localhost:8080/api/usuarios/buscar?rol=ADMIN
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json
{
  "username": "admin",
  "password": "nuevopass",
  "email": "admin@vet.com",
  "rol": "ADMIN"
}
DELETE http://localhost:8080/api/usuarios/1
```

### Veterinaria (ADMIN o VETERINARIO)

```
GET http://localhost:8080/api/veterinaria/consulta
GET http://localhost:8080/api/veterinaria/consulta?estado=PENDIENTE
GET http://localhost:8080/api/veterinaria/consulta/mascota/1
POST http://localhost:8080/api/veterinaria/consulta
Content-Type: application/json
{
  "mascota": {"id": 1},
  "sintomas": "Fiebre",
  "estado": "PENDIENTE"
}
PUT http://localhost:8080/api/veterinaria/consulta/1/atender
Content-Type: application/json
{
  "diagnostico": "Resfriado",
  "receta": "Paracetamol"
}
PUT http://localhost:8080/api/veterinaria/consulta/1/cancelar
GET http://localhost:8080/api/veterinaria/historia/1
POST http://localhost:8080/api/veterinaria/historia
Content-Type: application/json
{
  "mascota": {"id": 1},
  "motivoConsulta": "Chequeo general",
  "diagnostico": "Salud estable",
  "tratamiento": "Vitaminas"
}
DELETE http://localhost:8080/api/veterinaria/historia/1
POST http://localhost:8080/api/veterinaria/vacuna
Content-Type: application/json
{
  "mascota": {"id": 1},
  "nombreVacuna": "Rabia",
  "fechaAplicacion": "2026-05-15",
  "fechaProxima": "2027-05-15"
}
GET http://localhost:8080/api/veterinaria/vacuna/mascota/1
GET http://localhost:8080/api/veterinaria/vacuna/proximas
DELETE http://localhost:8080/api/veterinaria/vacuna/1
```

### Lavandería (ADMIN o EMPLEADO_LAVANDERIA)

```
GET http://localhost:8080/api/lavanderia/servicio
GET http://localhost:8080/api/lavanderia/servicio/1
GET http://localhost:8080/api/lavanderia/servicio/pendientes
POST http://localhost:8080/api/lavanderia/servicio
PATCH http://localhost:8080/api/lavanderia/servicio/1/estado
Content-Type: application/json
{
  "estado": "TERMINADO"
}
```

### Tienda - ventas (ADMIN o CLIENTE_TIENDA)

```
POST http://localhost:8080/api/tienda/ventas
Content-Type: application/json
{
  "detalles": [
    {
      "producto": {"id": 1},
      "cantidad": 2
    }
  ]
}
GET http://localhost:8080/api/tienda/ventas
GET http://localhost:8080/api/tienda/ventas/1
```

### Tienda - productos CRUD (solo ADMIN)

```
GET http://localhost:8080/api/tienda/productos/bajo-stock?limite=10
POST http://localhost:8080/api/tienda/productos
Content-Type: application/json
{
  "nombre": "Nuevo Producto",
  "precio": 99.99,
  "stock": 10,
  "categoria": "Accesorios"
}
PUT http://localhost:8080/api/tienda/productos/1
DELETE http://localhost:8080/api/tienda/productos/1
```

### Adopción - solicitudes (ADMIN o ADOPTANTE)

```
POST http://localhost:8080/api/adopcion/solicitudes
Content-Type: application/json
{
  "mascota": {"id": 1},
  "nombreSolicitante": "Juan",
  "telefono": "987654321",
  "motivo": "Quiero darle un hogar"
}
```

### Adopción - cambiar estado (solo ADMIN)

```
PATCH http://localhost:8080/api/adopcion/solicitudes/1/estado?estado=APROBADA
```

---

## 4. Registrar nuevo usuario

```
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "username": "nuevousuario",
  "password": "mipassword",
  "email": "usuario@mail.com",
  "rol": "CLIENTE_TIENDA"
}
```

Roles válidos: `ADMIN`, `VETERINARIO`, `EMPLEADO_LAVANDERIA`, `ADOPTANTE`, `CLIENTE_TIENDA`

---

## 5. Prueba de restricción por rol

Para verificar que la seguridad funciona:

1. Obtén token de `cliente` (CLIENTE_TIENDA)
2. Intenta acceder a un endpoint de ADMIN:

```
GET http://localhost:8080/api/usuarios
Authorization: Bearer <token_de_cliente>
```

**Respuesta esperada (403):**
```json
{
  "timestamp": "2026-05-26T...",
  "status": 403,
  "error": "Acceso denegado: no tienes permisos para esta operación"
}
```

---

## Tabla rápida de endpoints

| Método | Endpoint | Rol | Token |
|--------|----------|-----|-------|
| POST | `/api/usuarios/login` | público | No |
| POST | `/api/usuarios/registro` | público | No |
| GET | `/api/tienda/productos` | público | No |
| GET | `/api/tienda/productos/{id}` | público | No |
| GET | `/api/adopcion/disponibles` | público | No |
| GET | `/api/usuarios` | ADMIN | Sí |
| GET/PUT/DELETE | `/api/usuarios/{id}` | ADMIN | Sí |
| GET | `/api/usuarios/buscar` | ADMIN | Sí |
| POST | `/api/tienda/productos` | ADMIN | Sí |
| PUT/DELETE | `/api/tienda/productos/**` | ADMIN | Sí |
| GET | `/api/tienda/productos/bajo-stock` | ADMIN | Sí |
| PATCH | `/api/adopcion/solicitudes/**/estado` | ADMIN | Sí |
| GET/POST/DELETE | `/api/veterinaria/historia` | ADMIN, VETERINARIO | Sí |
| GET/POST | `/api/veterinaria/consulta` | ADMIN, VETERINARIO | Sí |
| PUT | `/api/veterinaria/consulta/**/atender` | ADMIN, VETERINARIO | Sí |
| PUT | `/api/veterinaria/consulta/**/cancelar` | ADMIN, VETERINARIO | Sí |
| GET/POST/DELETE | `/api/veterinaria/vacuna` | ADMIN, VETERINARIO | Sí |
| GET | `/api/veterinaria/vacuna/proximas` | ADMIN, VETERINARIO | Sí |
| GET/POST | `/api/lavanderia/servicio` | ADMIN, EMPLEADO_LAVANDERIA | Sí |
| PATCH | `/api/lavanderia/servicio/**/estado` | ADMIN, EMPLEADO_LAVANDERIA | Sí |
| POST | `/api/tienda/ventas` | ADMIN, CLIENTE_TIENDA | Sí |
| GET | `/api/tienda/ventas` | ADMIN, CLIENTE_TIENDA | Sí |
| GET | `/api/tienda/ventas/{id}` | ADMIN, CLIENTE_TIENDA | Sí |
| POST | `/api/adopcion/solicitudes` | ADMIN, ADOPTANTE | Sí |
