# 📋 Guía de Pruebas de Endpoints (Postman / REST Client)

Este documento contiene una lista de todos los endpoints de la API del proyecto Veterinaria, organizados por categorías. Se han actualizado las respuestas y solicitudes utilizando los **DTOs planos** para evitar errores de serialización.

> [!NOTE]
> Para los endpoints protegidos, primero debes hacer login (`POST /api/usuarios/login`), copiar el `token` devuelto y colocarlo en los headers de tus siguientes solicitudes como: `Authorization: Bearer <tu_token_aquí>`.

---

## 1. Módulo de Autenticación y Usuarios

### Registrar nuevo usuario (Público)
* **URL:** `POST http://localhost:8080/api/usuarios/registro`
* **Cuerpo de la Petición (JSON):**
```json
{
  "username": "nuevousuario",
  "password": "mipassword",
  "email": "usuario@mail.com",
  "rol": "CLIENTE_TIENDA"
}
```
* **Roles Válidos:** `ADMIN`, `VETERINARIO`, `EMPLEADO_LAVANDERIA`, `ADOPTANTE`, `CLIENTE_TIENDA`
* **Respuesta Exitosa (200):**
```json
{
  "id": 2,
  "username": "nuevousuario",
  "email": "usuario@mail.com",
  "rol": "CLIENTE_TIENDA",
  "activo": true,
  "fechaCreacion": "2026-06-11T12:00:00"
}
```

---

### Obtener Token / Login (Público)
* **URL:** `POST http://localhost:8080/api/usuarios/login`
* **Cuerpo de la Petición (JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
* **Respuesta Exitosa (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "rol": "ADMIN"
}
```

---

### Listar Usuarios (Solo ADMIN)
* **URL:** `GET http://localhost:8080/api/usuarios`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@vet.com",
    "rol": "ADMIN",
    "activo": true,
    "fechaCreacion": "2026-06-11T11:00:00"
  }
]
```

---

## 2. Módulo de Veterinaria (Solo ADMIN o VETERINARIO)

### Registrar Consulta (POST)
* **URL:** `POST http://localhost:8080/api/veterinaria/consulta`
* **Cuerpo de la Petición (JSON):**
```json
{
  "mascota": {"id": 1},
  "sintomas": "Fiebre y letargia"
}
```
* **Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "mascotaId": 1,
  "nombreMascota": "Firu",
  "veterinarioId": null,
  "nombreVeterinario": null,
  "sintomas": "Fiebre y letargia",
  "diagnostico": null,
  "receta": null,
  "estado": "PENDIENTE",
  "fecha": "2026-06-11T12:05:00"
}
```

---

### Listar Todas las Consultas (GET)
* **URL:** `GET http://localhost:8080/api/veterinaria/consulta`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "mascotaId": 1,
    "nombreMascota": "Firu",
    "veterinarioId": null,
    "nombreVeterinario": null,
    "sintomas": "Fiebre y letargia",
    "diagnostico": null,
    "receta": null,
    "estado": "PENDIENTE",
    "fecha": "2026-06-11T12:05:00"
  }
]
```

---

### Atender Consulta (PUT)
* **URL:** `PUT http://localhost:8080/api/veterinaria/consulta/1/atender`
* **Cuerpo de la Petición (JSON):**
```json
{
  "diagnostico": "Gripe canina",
  "receta": "Paracetamol 100mg cada 12 horas"
}
```
* **Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "mascotaId": 1,
  "nombreMascota": "Firu",
  "veterinarioId": 1,
  "nombreVeterinario": "admin",
  "sintomas": "Fiebre y letargia",
  "diagnostico": "Gripe canina",
  "receta": "Paracetamol 100mg cada 12 horas",
  "estado": "REALIZADA",
  "fecha": "2026-06-11T12:05:00"
}
```

---

### Obtener Historia Clínica de Mascota (GET)
* **URL:** `GET http://localhost:8080/api/veterinaria/historia/1`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "mascotaId": 1,
    "nombreMascota": "Firu",
    "motivoConsulta": "Chequeo general",
    "diagnostico": "Salud estable",
    "tratamiento": "Vitaminas",
    "fechaCreacion": "2026-06-11T11:30:00"
  }
]
```

---

## 3. Módulo de Lavandería (ADMIN o EMPLEADO_LAVANDERIA)

### Listar Servicios de Lavado (GET)
* **URL:** `GET http://localhost:8080/api/lavanderia/servicio`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "mascotaId": 1,
    "nombreMascota": "Firu",
    "tipoServicio": "LAVADO_Y_CORTE",
    "precio": 35.0,
    "fechaHora": "2026-06-11T14:00:00",
    "estado": "PENDIENTE",
    "observaciones": "Sin observaciones"
  }
]
```

---

### Registrar Servicio de Lavado (POST)
* **URL:** `POST http://localhost:8080/api/lavanderia/servicio`
* **Cuerpo de la Petición (JSON):**
```json
{
  "mascota": {"id": 1},
  "tipoServicio": "LAVADO_COMPLETO",
  "precio": 25.0,
  "fechaHora": "2026-06-11T15:30:00",
  "estado": "PENDIENTE",
  "observaciones": "Pelo enredado"
}
```
* **Respuesta Exitosa (201):**
```json
{
  "id": 2,
  "mascotaId": 1,
  "nombreMascota": "Firu",
  "tipoServicio": "LAVADO_COMPLETO",
  "precio": 25.0,
  "fechaHora": "2026-06-11T15:30:00",
  "estado": "PENDIENTE",
  "observaciones": "Pelo enredado"
}
```

---

### Cambiar Estado del Servicio (PATCH)
* **URL:** `PATCH http://localhost:8080/api/lavanderia/servicio/1/estado`
* **Cuerpo de la Petición (JSON):**
```json
{
  "estado": "TERMINADO"
}
```
* **Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "mascotaId": 1,
  "nombreMascota": "Firu",
  "tipoServicio": "LAVADO_Y_CORTE",
  "precio": 35.0,
  "fechaHora": "2026-06-11T14:00:00",
  "estado": "TERMINADO",
  "observaciones": "Sin observaciones"
}
```

---

## 4. Módulo Tienda (Productos y Ventas)

### Listar Productos (Público - GET)
* **URL:** `GET http://localhost:8080/api/tienda/productos`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "nombre": "Shampoo Antipulgas",
    "precio": 15.5,
    "stock": 50,
    "categoria": "Higiene",
    "fechaRegistro": "2026-06-11T11:00:00"
  }
]
```

---

### Registrar Venta (ADMIN o CLIENTE_TIENDA - POST)
* **URL:** `POST http://localhost:8080/api/tienda/ventas`
* **Cuerpo de la Petición (JSON):**
```json
{
  "detalles": [
    {
      "producto": {"id": 1},
      "cantidad": 2
    }
  ]
}
```
* **Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "fecha": "2026-06-11T12:15:00",
  "total": 31.0,
  "detalles": [
    {
      "id": 1,
      "productoId": 1,
      "nombreProducto": "Shampoo Antipulgas",
      "cantidad": 2,
      "precioUnitario": 15.5,
      "subtotal": 31.0
    }
  ]
}
```

---

### Listar Todas las Ventas (ADMIN o CLIENTE_TIENDA - GET)
* **URL:** `GET http://localhost:8080/api/tienda/ventas`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "fecha": "2026-06-11T12:15:00",
    "total": 31.0,
    "detalles": [
      {
        "id": 1,
        "productoId": 1,
        "nombreProducto": "Shampoo Antipulgas",
        "cantidad": 2,
        "precioUnitario": 15.5,
        "subtotal": 31.0
      }
    ]
  }
]
```

---

## 5. Módulo Adopción

### Listar Mascotas Disponibles para Adopción (Público - GET)
* **URL:** `GET http://localhost:8080/api/adopcion/disponibles`
* **Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "nombre": "Bobby",
    "especie": "Perro",
    "edad": 2,
    "descripcion": "Muy cariñoso y juguetón",
    "disponible": true
  }
]
```

---

### Enviar Solicitud de Adopción (ADMIN o ADOPTANTE - POST)
* **URL:** `POST http://localhost:8080/api/adopcion/solicitudes`
* **Cuerpo de la Petición (JSON):**
```json
{
  "mascota": {"id": 1},
  "nombreSolicitante": "Juan Pérez",
  "telefono": "987654321",
  "motivo": "Quiero darle un hogar lleno de amor"
}
```
* **Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "nombreSolicitante": "Juan Pérez",
  "telefono": "987654321",
  "motivo": "Quiero darle un hogar lleno de amor",
  "estado": "PENDIENTE",
  "mascotaId": 1,
  "nombreMascota": "Bobby"
}
```
