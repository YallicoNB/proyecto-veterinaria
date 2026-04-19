# Proyecto Veterinaria - Clínica Veterinaria Multi-Servicio

Este es un proyecto académico de una **clínica veterinaria** desarrollado con **Spring Boot** y **Java**. El sistema ofrece múltiples servicios:

- **Veterinaria**: Consultas, vacunas e historias clínicas
- **Lavandería**: Servicio de baño y limpieza para mascotas
- **Adopción**: Gestión de mascotas en adopción
- **Tienda**: Inventario y ventas de productos para mascotas

## Por qué tiene esta estructura?

Este proyecto usa **Maven multi-module**, una arquitectura donde el proyecto se divide en **módulos independientes**. Esto permite que los 5 integrantes del grupo trabajen simultáneamente sin interferir entre sí, cada uno en su propio módulo.

La estructura es:

```
proyecto-veterinaria/
├── modulo-common/        (entidades compartidas por todos)
├── modulo-usuarios/      (integrante 1 - puerto 8081)
├── modulo-veterinaria/   (integrante 2 - puerto 8082)
├── modulo-lavanderia/    (integrante 3 - puerto 8083)
├── modulo-adopcion/     (integrante 4 - puerto 8084)
└── modulo-tienda/      (integrante 5 - puerto 8085)
```

**modulo-common** contiene las entidades (`Usuario`, `Mascota`) y repositorios que todos comparten. Cada integrante desarrolla su propio módulo (modelos, servicios, controladores).

Cada módulo corre en un **puerto diferente** para evitar conflictos.

---

## Requisitos para ejecutar

- Java JDK 17
- Maven 3.9+

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

### 3. Ejecutar tu módulo

Cada integrante trabaja en su módulo específico:

| Integrante | Módulo | Puerto | Comando |
|------------|-------|--------|---------|
| 1 | usuarios | 8081 | `cd modulo-usuarios && mvn spring-boot:run` |
| 2 | veterinaria | 8082 | `cd modulo-veterinaria && mvn spring-boot:run` |
| 3 | lavandería | 8083 | `cd modulo-lavanderia && mvn spring-boot:run` |
| 4 | adopción | 8084 | `cd modulo-adopcion && mvn spring-boot:run` |
| 5 | tienda | 8085 | `cd modulo-tienda && mvn spring-boot:run` |

## Reglas de trabajo

1. Si modificas **modulo-common**, ejecuta `mvn clean install -DskipTests` desde la raíz **antes** de correr tu módulo
2. Si modificas solo tu módulo, haz `mvn clean` y luego `mvn spring-boot:run` dentro de tu carpeta
3. Antes de hacer cambios, haz `git pull origin master` para traer los cambios de tus compañeros

## Cómo contribuir

```bash
# Hacer cambios
git add .
git commit -m "Descripción del cambio"
git push origin master
```

## Tecnologías usadas

- Spring Boot 3.2.0
- Java 17
- Maven
- H2 Database (desarrollo)
- Spring Security
- JWT

---

**Proyecto desarrollado por el equipo de Desarrollo Web Integrado**