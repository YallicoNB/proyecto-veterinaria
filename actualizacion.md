# 🚀 Actualización de Arquitectura del Backend: Proyecto Veterinaria

Este documento detalla los cambios estructurales y arquitectónicos realizados en las capas principales del proyecto (Modelos, Repositorios, Servicios y Controladores) con el objetivo de modernizar la API, mejorar la seguridad, el rendimiento y la escalabilidad del sistema.

---

## 🏗️ 1. Capa de Modelos (Entities y DTOs)

### ¿Qué se cambió?

- **Implementación del patrón DTO (Data Transfer Object):** Se separaron los modelos de base de datos (`Entities`) de los objetos que se exponen en la API (`DTOs`).
- **Integración de Lombok:** Se eliminaron getters, setters, constructores y métodos `toString()` o `equals()` manuales, reemplazándolos por anotaciones de Lombok (`@Data`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`).
- **Mapeo Relacional JPA:** Se configuraron correctamente las relaciones de base de datos (`@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`) evitando ciclos infinitos de serialización.
- **Validaciones:** Se implementó Jakarta Bean Validation (`@NotNull`, `@Size`, `@Email`, etc.) directamente en los DTOs.

### 🤔 ¿Por qué se hizo el cambio?

1. **Seguridad:** Exponer directamente las entidades de la base de datos (Entities) al cliente puede revelar información sensible (como contraseñas, tokens o relaciones internas). Los DTOs permiten controlar exactamente qué datos se envían y reciben.
2. **Evitar Ciclos Infinitos:** Al usar relaciones bidireccionales en JPA (ej. un `Mascota` tiene un `Usuario` y un `Usuario` tiene muchas `Mascotas`), serializar la entidad directamente en JSON generaba un ciclo infinito. Los DTOs rompen este ciclo.
3. **Código más limpio:** Lombok reduce drásticamente el "código repetitivo" (boilerplate), haciendo que los modelos sean más fáciles de leer y mantener.
4. **Validación robusta:** Validar los datos antes de que lleguen a la capa de negocio previene errores de base de datos y ataques comunes.

---

## 2. Capa de Repositorios (Repositories)

### ¿Qué se cambió?

- **Uso estándar de Spring Data JPA:** Las interfaces ahora extienden `JpaRepository<Entidad, ID>` y no implementaciones manuales.
- **Consultas personalizadas (Query Methods):** Se agregaron métodos que Spring Data interpreta automáticamente (ej. `findByEmail`, `findByEstadoActivoTrue`) sin necesidad de escribir SQL puro.

### 🤔 ¿Por qué se hizo el cambio?

1. **Productividad:** `JpaRepository` provee todos los métodos CRUD básicos (crear, leer, actualizar, borrar) y de paginación de forma gratuita, sin necesidad de escribir la implementación.
2. **Mantenibilidad:** Menos código escrito a mano significa menos posibilidad de errores y bugs en las consultas a la base de datos.

---

## ⚙️ 3. Capa de Servicios (Services)

### ¿Qué se cambió?

- **Lógica de Mapeo:** Los servicios ahora son los encargados de traducir o "mapear" la información que llega de los Controladores (DTOs) hacia los Repositorios (Entities) y viceversa.
- **Inyección de dependencias por constructor:** Se recomendó el uso de inyección a través de constructores (o usando `@RequiredArgsConstructor` de Lombok) en lugar de `@Autowired` en los campos.
- **Manejo de Excepciones de Negocio:** Validación de reglas de negocio antes de guardar en la base de datos (ej. "Esta mascota ya tiene una cita en ese horario").

### 🤔 ¿Por qué se hizo el cambio?

1. **Desacoplamiento:** El Controlador no necesita saber cómo funciona la base de datos, y el Repositorio no necesita saber cómo se presenta la información en la web. El Servicio actúa como el orquestador perfecto entre ambos mundos.
2. **Buenas Prácticas de Spring:** La inyección por constructor facilita la creación de pruebas unitarias y hace que las dependencias sean inmutables, evitando problemas de estado en ejecución.

---

## 🌐 4. Capa de Controladores (Controllers)

### ¿Qué se cambió?

- **Uso exclusivo de DTOs en las peticiones y respuestas:** Todos los `@RequestBody` y retornos de los métodos ahora usan clases DTO.
- **Estándares RESTful:** Se organizaron las rutas correctamente (`/api/v1/...`) y se utilizaron los verbos HTTP adecuados (`GET`, `POST`, `PUT`, `DELETE`).
- **Manejo de Códigos de Estado HTTP:** Uso de `ResponseEntity` para retornar explícitamente `201 Created`, `200 OK`, `404 Not Found` o `400 Bad Request` según corresponda.
- **Validación de entrada:** Uso de la anotación `@Valid` junto al `@RequestBody` para activar las validaciones definidas en los DTOs.

### 🤔 ¿Por qué se hizo el cambio?

1. **Profesionalismo y Estándares:** Una API REST debe ser predecible. Retornar el código de estado HTTP correcto ayuda a que el Frontend (React, Angular, Móvil) maneje los errores y respuestas correctamente.
2. **Preparación para Frontend:** Al retornar estructuras JSON limpias (DTOs) en lugar de objetos complejos anidados de JPA, la integración con cualquier framework frontend será mucho más rápida y sencilla.
3. **Documentación Automática (Postman/Swagger):** Con endpoints predecibles, es muy sencillo exportar la estructura a Postman para pruebas de integración o generar documentación con Swagger/OpenAPI.

---

### 📋 Resumen del Flujo de Trabajo Actualizado:

1. El Cliente envía un JSON al **Controller**.
2. El **Controller** lo recibe y valida automáticamente como un **DTO** de entrada (`RequestDTO`).
3. El Controller pasa el DTO al **Service**.
4. El **Service** convierte el DTO a un **Entity**, aplica lógica de negocio y lo guarda mediante el **Repository**.
5. El **Service** recupera la información de la base de datos (Entity), la convierte en un **DTO** de salida (`ResponseDTO`) y la devuelve.
6. El **Controller** recibe el DTO de salida y lo responde al cliente en formato JSON con el código HTTP correspondiente.
