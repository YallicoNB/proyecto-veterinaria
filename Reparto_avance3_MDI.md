# 📋 Reparto de Tareas — Avance 3 (Frontend Angular)

**Curso:** Desarrollo Web Integrado  
**Proyecto:** Clínica Veterinaria Multi-Servicio  
**Backend:** Spring Boot 3.2.0 (Java 21)  
**Frontend:** Angular 17+ (Standalone Components)  
**Equipo:** 4 Developers  

---

## 📐 Convenciones de Código (TODOS)

| Regla | Estándar |
|-------|----------|
| Componentes | Standalone (`ng new --standalone`) |
| Control de flujo | `@if` / `@for` (NO `*ngIf` / `*ngFor`) |
| Inyección | `inject()` (NO constructor DI) |
| Estilos | SCSS |
| Modelos | Interfaces TypeScript en `src/app/models/` |
| API Base | `http://localhost:8080/api` |

---

## 🏗️ Developer 1 — Core + Módulo Auth (Usuarios)

### Sesiones que aplica

| Sesión | Contenido |
|--------|-----------|
| S11 | Crear proyecto Angular, configurar SCSS, componentes layout |
| S12 | Routing principal, `router-outlet`, `routerLink`, guards |
| S13 | Formularios reactivos para login y registro, validaciones |
| S14 | AuthService, HttpClient, JWT interceptor, guards por rol |

### Tareas

| # | Tarea | Archivos esperados |
|---|-------|--------------------|
| 1 | Inicializar proyecto con `ng new veterinaria-front --standalone --routing --style=scss` | `angular.json`, `package.json` |
| 2 | Crear modelos TypeScript: `Usuario`, `AuthResponse`, `Rol` enum | `src/app/models/usuario.model.ts`, `src/app/models/auth-response.model.ts` |
| 3 | Crear layout base: `NavbarComponent` (con menú según rol), `FooterComponent` | `src/app/components/navbar/`, `src/app/components/footer/` |
| 4 | Configurar rutas principales en `app.routes.ts`: `/login`, `/register`, `/usuarios`, `/dashboard` | `src/app/app.routes.ts` |
| 5 | Implementar `AuthService` con `login()`, `register()`, `logout()`, `getToken()`, `getRol()`, `isAuthenticated()` | `src/app/services/auth.service.ts` |
| 6 | Implementar `JwtInterceptor` para añadir `Authorization: Bearer <token>` | `src/app/interceptors/jwt.interceptor.ts` |
| 7 | Implementar guards: `authGuard` (redirige a /login si no autenticado), `roleGuard` (restringe por rol) | `src/app/guards/auth.guard.ts`, `src/app/guards/role.guard.ts` |
| 8 | Crear `LoginComponent` con formulario reactivo (username, password, validaciones, mostrar error) | `src/app/pages/login/` |
| 9 | Crear `RegisterComponent` con formulario reactivo (username, password, email, rol, validaciones) | `src/app/pages/register/` |
| 10 | Crear `UsuarioListComponent` con tabla CRUD (solo ADMIN), botones editar/eliminar | `src/app/pages/usuarios/` |
| 11 | Crear `UsuarioFormComponent` para editar usuario existente | `src/app/pages/usuarios/` |
| 12 | Implementar `UsuarioService` con métodos: `listar()`, `buscarPorId()`, `crear()`, `actualizar()`, `eliminar()` | `src/app/services/usuario.service.ts` |
| 13 | Aplicar SCSS consistente (variables de colores, mixins) | `src/styles.scss` |

### Endpoints que consume

| Método | Endpoint |
|--------|----------|
| POST | `/api/usuarios/registro` |
| POST | `/api/usuarios/login` |
| GET | `/api/usuarios` |
| GET | `/api/usuarios/{id}` |
| PUT | `/api/usuarios/{id}` |
| DELETE | `/api/usuarios/{id}` |

---

## 🛒 Developer 2 — Módulo Tienda (Productos + Ventas)

### Sesiones que aplica

| Sesión | Contenido |
|--------|-----------|
| S11 | Componentes con `@for`, `@if`, pipes `currency`, estilos SCSS |
| S12 | Rutas `/tienda/productos`, `/tienda/ventas`, pipes personalizados |
| S13 | Formulario reactivo con `FormArray` para detalles de venta |
| S14 | ProductoService, VentaService con HttpClient |

### Tareas

| # | Tarea | Archivos esperados |
|---|-------|--------------------|
| 1 | Crear modelos: `Producto`, `Venta`, `DetalleVenta`, `VentaRequest`, `DetalleVentaRequest` | `src/app/models/producto.model.ts`, `src/app/models/venta.model.ts` |
| 2 | Implementar `ProductoService`: `listar()`, `buscarPorId()`, `crear()`, `actualizar()`, `eliminar()`, `bajoStock(limite)` | `src/app/services/producto.service.ts` |
| 3 | Implementar `VentaService`: `listar()`, `buscarPorId()`, `registrar()` | `src/app/services/venta.service.ts` |
| 4 | Crear `ProductoListComponent` con tabla, filtro por nombre/bajo stock, pipe `currency` | `src/app/pages/tienda/productos/` |
| 5 | Crear `ProductoFormComponent` para crear/editar producto (campo: nombre, precio, stock, categoria) | `src/app/pages/tienda/productos/` |
| 6 | Crear `VentaFormComponent` con formulario reactivo + `FormArray` para detalles dinámicos (seleccionar producto, cantidad, calcular subtotal) | `src/app/pages/tienda/ventas/` |
| 7 | Crear `VentaListComponent` con tabla de ventas, expandir para ver detalles | `src/app/pages/tienda/ventas/` |
| 8 | Crear pipe personalizado `filterProductos` (o usar método en componente) | `src/app/pipes/filter.pipe.ts` |
| 9 | Configurar rutas hijas: `/tienda/productos`, `/tienda/productos/nuevo`, `/tienda/productos/:id/editar`, `/tienda/ventas`, `/tienda/ventas/nueva` | `src/app/pages/tienda/tienda.routes.ts` |

### Endpoints que consume

| Método | Endpoint |
|--------|----------|
| GET | `/api/tienda/productos` |
| GET | `/api/tienda/productos/{id}` |
| POST | `/api/tienda/productos` |
| PUT | `/api/tienda/productos/{id}` |
| DELETE | `/api/tienda/productos/{id}` |
| GET | `/api/tienda/productos/bajo-stock?limite=10` |
| POST | `/api/tienda/ventas` |
| GET | `/api/tienda/ventas` |
| GET | `/api/tienda/ventas/{id}` |

---

## 🐾 Developer 3 — Módulos Veterinaria + Lavandería

### Sesiones que aplica

| Sesión | Contenido |
|--------|-----------|
| S11 | Componentes listado con `@for`, estilos SCSS |
| S12 | Rutas `/veterinaria/*`, `/lavanderia/*`, pipes |
| S13 | Formularios reactivos para consultas, vacunas, servicios lavado |
| S14 | ConsultaService, VacunaService, LavanderiaService con HttpClient |

### Tareas

| # | Tarea | Archivos esperados |
|---|-------|--------------------|
| 1 | Crear modelos: `HistoriaClinica`, `Consulta`, `Vacuna`, `ServicioLavado`, `Mascota` + enums (`EstadoConsulta`, `TipoServicio`) | `src/app/models/` |
| 2 | Implementar `ConsultaService`: `listar(estado?)`, `buscarPorMascota()`, `agendar()`, `atender()`, `cancelar()` | `src/app/services/consulta.service.ts` |
| 3 | Implementar `VacunaService`: `registrar()`, `buscarPorMascota()`, `proximas()`, `eliminar()` | `src/app/services/vacuna.service.ts` |
| 4 | Implementar `HistoriaClinicaService`: `buscarPorMascota()`, `crear()`, `eliminar()` | `src/app/services/historia-clinica.service.ts` |
| 5 | Implementar `LavanderiaService`: `listar()`, `buscarPorId()`, `crear()`, `cambiarEstado()`, `listarPendientes()` | `src/app/services/lavanderia.service.ts` |
| 6 | Crear `MascotaService` o reutilizar para busqueda por ID | `src/app/services/mascota.service.ts` |
| 7 | Crear `ConsultaListComponent` con filtro por estado (PENDIENTE/REALIZADA/CANCELADA) | `src/app/pages/veterinaria/consultas/` |
| 8 | Crear `ConsultaFormComponent` para agendar nueva consulta (mascotaId, sintomas) | `src/app/pages/veterinaria/consultas/` |
| 9 | Crear `AtenderConsultaComponent` para diagnosticar (diagnostico, receta) | `src/app/pages/veterinaria/consultas/` |
| 10 | Crear `VacunaListComponent` con listado por mascota + próximas vacunas | `src/app/pages/veterinaria/vacunas/` |
| 11 | Crear `VacunaFormComponent` para registrar vacuna | `src/app/pages/veterinaria/vacunas/` |
| 12 | Crear `HistoriaClinicaComponent` para ver historial de una mascota | `src/app/pages/veterinaria/historia/` |
| 13 | Crear `LavanderiaListComponent` con tabla de servicios, filtro pendientes | `src/app/pages/lavanderia/` |
| 14 | Crear `LavanderiaFormComponent` para crear servicio de lavado | `src/app/pages/lavanderia/` |
| 15 | Configurar rutas hijas para veterinaria y lavanderia | `src/app/pages/veterinaria/veterinaria.routes.ts`, `src/app/pages/lavanderia/lavanderia.routes.ts` |

### Endpoints que consume

| Método | Endpoint |
|--------|----------|
| GET/POST/DELETE | `/api/veterinaria/historia/**` |
| GET/POST/PUT | `/api/veterinaria/consulta/**` |
| GET/POST/DELETE | `/api/veterinaria/vacuna/**` |
| GET/POST/PATCH | `/api/lavanderia/servicio/**` |

---

## 🏡 Developer 4 — Módulo Adopción + Integración General

### Sesiones que aplica

| Sesión | Contenido |
|--------|-----------|
| S11 | Página pública de mascotas disponibles con `@for` |
| S12 | Rutas `/adopcion`, navegación completa, `routerLinkActive` |
| S13 | Formulario de solicitud de adopción con validaciones |
| S14 | AdopcionService con HttpClient |
| S15 | Integración total: RxJS, datos dinámicos, dashboard, pruebas manuales |

### Tareas

| # | Tarea | Archivos esperados |
|---|-------|--------------------|
| 1 | Crear modelos: `MascotaAdoptable`, `SolicitudAdopcion`, `SolicitudAdopcionRequest` | `src/app/models/` |
| 2 | Implementar `AdopcionService`: `listarDisponibles()`, `enviarSolicitud()`, `cambiarEstado()` | `src/app/services/adopcion.service.ts` |
| 3 | Crear `MascotaAdoptableListComponent` (página pública) con tarjetas, foto, descripción | `src/app/pages/adopcion/` |
| 4 | Crear `SolicitudFormComponent` formulario reactivo (mascotaId, nombreSolicitante, telefono, motivo) | `src/app/pages/adopcion/` |
| 5 | Crear `SolicitudListComponent` (solo ADMIN) para ver y aprobar/rechazar solicitudes | `src/app/pages/adopcion/` |
| 6 | Crear `DashboardComponent` con resumen: cards con cantidades (productos, servicios, solicitudes) | `src/app/pages/dashboard/` |
| 7 | Integrar observables RxJS: `combineLatest` o `switchMap` para datos dinámicos en dashboard | `src/app/pages/dashboard/` |
| 8 | Configurar `provideHttpClient(withInterceptors([jwtInterceptor]))` en `app.config.ts` | `src/app/app.config.ts` |
| 9 | Asegurar navbar responsivo con `routerLinkActive` para cada sección | `src/app/components/navbar/` |
| 10 | Probar integración completa: flujo login → navegar a cada módulo → CRUD → logout | Manual |
| 11 | Verificar estilo visual consistente (SCSS variables, colores, tipografía) | `src/styles.scss` |

### Endpoints que consume

| Método | Endpoint |
|--------|----------|
| GET | `/api/adopcion/disponibles` |
| POST | `/api/adopcion/solicitudes` |
| PATCH | `/api/adopcion/solicitudes/{id}/estado?estado=...` |

---

## 🗺️ Plano de Rutas (Frontend)

```
/dashboard                   → DashboardComponent        [ADMIN, VETERINARIO, EMPLEADO_LAVANDERIA, CLIENTE_TIENDA, ADOPTANTE]
/login                       → LoginComponent            [PUBLICO]
/register                    → RegisterComponent         [PUBLICO]

/usuarios                    → UsuarioListComponent      [ADMIN]
/usuarios/:id/editar         → UsuarioFormComponent      [ADMIN]

/tienda/productos            → ProductoListComponent     [PUBLICO]
/tienda/productos/nuevo      → ProductoFormComponent     [ADMIN]
/tienda/productos/:id/editar → ProductoFormComponent     [ADMIN]
/tienda/ventas               → VentaListComponent        [ADMIN, CLIENTE_TIENDA]
/tienda/ventas/nueva         → VentaFormComponent        [ADMIN, CLIENTE_TIENDA]

/veterinaria/consultas       → ConsultaListComponent     [ADMIN, VETERINARIO]
/veterinaria/consultas/nueva → ConsultaFormComponent     [ADMIN, VETERINARIO]
/veterinaria/consultas/:id   → AtenderConsultaComponent  [ADMIN, VETERINARIO]
/veterinaria/vacunas         → VacunaListComponent       [ADMIN, VETERINARIO]
/veterinaria/vacunas/nueva   → VacunaFormComponent       [ADMIN, VETERINARIO]
/veterinaria/historia/:id    → HistoriaClinicaComponent  [ADMIN, VETERINARIO]

/lavanderia/servicios        → LavanderiaListComponent   [ADMIN, EMPLEADO_LAVANDERIA]
/lavanderia/servicios/nuevo  → LavanderiaFormComponent   [ADMIN, EMPLEADO_LAVANDERIA]

/adopcion/disponibles        → MascotaAdoptableListComponent  [PUBLICO]
/adopcion/solicitudes/nueva  → SolicitudFormComponent         [ADMIN, ADOPTANTE]
/adopcion/solicitudes        → SolicitudListComponent         [ADMIN]
```

---

## 📦 Orden Sugerido de Implementación

```
Semana 1:
  Dev1: Inicializa proyecto, crea modelos, auth service, navbar
  Dev2: Crea modelos tienda, product service
  Dev3: Crea modelos veterinaria, servicios base
  Dev4: Crea modelos adopción, adopcion service, dashboard

Semana 2:
  Dev1: Login/Register components, interceptors, guards
  Dev2: Producto list/form components
  Dev3: Consulta list/form components
  Dev4: MascotaAdoptable list, solicitud form

Semana 3:
  Dev1: Usuario CRUD, SCSS variables globales
  Dev2: Venta form con FormArray, venta list
  Dev3: Vacuna, historia, lavanderia components
  Dev4: Solicitud list (admin), navbar completo

Semana 4:
  Todos: Integración, pruebas de flujo completo, corrección de errores
  Dev4: Dashboard con RxJS, polish final SCSS
```

---

## 🔗 Referencia Rápida de la API

| Módulo | Base URL |
|--------|----------|
| Usuarios | `/api/usuarios` |
| Tienda | `/api/tienda` |
| Veterinaria | `/api/veterinaria` |
| Lavandería | `/api/lavanderia/servicio` |
| Adopción | `/api/adopcion` |

**Base URL completa:** `http://localhost:8080/api`

**Autenticación:** `Authorization: Bearer <token>` (obtenido de `/api/usuarios/login`)
