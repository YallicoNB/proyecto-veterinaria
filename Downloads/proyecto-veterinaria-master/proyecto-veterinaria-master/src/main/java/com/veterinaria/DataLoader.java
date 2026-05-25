package com.veterinaria;

import com.veterinaria.model.*;
import com.veterinaria.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            MascotaRepository mascotaRepository,
            ProductoRepository productoRepository,
            MascotaAdoptableRepository mascotaAdoptableRepository,
            ServicioLavadoRepository servicioLavadoRepository,
            ConsultaRepository consultaRepository,
            VacunaRepository vacunaRepository,
            HistoriaClinicaRepository historiaClinicaRepository
    ) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                List<Usuario> usuarios = Arrays.asList(
                    new Usuario("admin", "admin123", "admin@vet.com", Rol.ADMIN),
                    new Usuario("veterinario", "vet123", "veterinario@vet.com", Rol.VETERINARIO),
                    new Usuario("cliente", "cliente123", "cliente@vet.com", Rol.CLIENTE_TIENDA),
                    new Usuario("empleado", "empleado123", "empleado@vet.com", Rol.EMPLEADO_LAVANDERIA)
                );
                usuarioRepository.saveAll(usuarios);
                System.out.println(">>> Usuarios cargados: " + usuarios.size());
            }

            if (mascotaRepository.count() == 0) {
                List<Mascota> mascotas = Arrays.asList(
                    crearMascota("Max", TipoMascota.PERRO, "Labrador", 3, Sexo.MACHO, "Dorado", 25.5),
                    crearMascota("Luna", TipoMascota.PERRO, "Golden Retriever", 2, Sexo.HEMBRA, "Crema", 22.0),
                    crearMascota("Simba", TipoMascota.GATO, "Persa", 4, Sexo.MACHO, "Naranja", 4.5),
                    crearMascota("Mika", TipoMascota.GATO, "Siamés", 1, Sexo.HEMBRA, "Crema", 3.0),
                    crearMascota("Buddy", TipoMascota.PERRO, "Bulldog", 5, Sexo.MACHO, "Blanco", 18.0),
                    crearMascota("Nala", TipoMascota.GATO, "Mestizo", 2, Sexo.HEMBRA, "Negro", 3.5)
                );
                mascotaRepository.saveAll(mascotas);
                System.out.println(">>> Mascotas cargadas: " + mascotas.size());
            }

            if (productoRepository.count() == 0) {
                List<Producto> productos = Arrays.asList(
                    crearProducto("Alimento Perro Adulto", 450.00, 50, "Alimentos"),
                    crearProducto("Alimento Gato Kitten", 380.00, 30, "Alimentos"),
                    crearProducto("Jaula para Perros", 1200.00, 15, "Accesorios"),
                    crearProducto("Cama para Mascota", 350.00, 25, "Camas"),
                    crearProducto("Juguete Pelota", 45.00, 100, "Juguetes"),
                    crearProducto("Correa Reflectante", 120.00, 60, "Accesorios"),
                    crearProducto("Shampoo Medicado", 280.00, 40, "Cuidado"),
                    crearProducto("Vitaminas", 190.00, 35, "Suplementos"),
                    crearProducto("Comedero Automático", 550.00, 20, "Accesorios"),
                    crearProducto("Cepillo para Pelaje", 95.00, 45, "Cuidado")
                );
                productoRepository.saveAll(productos);
                System.out.println(">>> Productos cargados: " + productos.size());
            }

            if (mascotaAdoptableRepository.count() == 0) {
                List<MascotaAdoptable> adoptables = Arrays.asList(
                    crearMascotaAdoptable("Thor", "Perro", 2, "Labrador noble y amigable"),
                    crearMascotaAdoptable("Luki", "Gato", 1, "Gato tranquilo y cariñoso"),
                    crearMascotaAdoptable("Rocky", "Perro", 4, "Boxer jugador y activo")
                );
                mascotaAdoptableRepository.saveAll(adoptables);
                System.out.println(">>> Mascotas en adopción: " + adoptables.size());
            }

            List<Mascota> mascotas = mascotaRepository.findAll();
            List<Usuario> usuariosList = usuarioRepository.findAll();
            Usuario vet = usuariosList.stream().filter(u -> u.getRol() == Rol.VETERINARIO).findFirst().orElse(usuariosList.get(0));

            if (!mascotas.isEmpty()) {
                if (servicioLavadoRepository.count() == 0) {
                    ServicioLavado s1 = new ServicioLavado();
                    s1.setMascota(mascotas.get(0));
                    s1.setTipoServicio(TipoServicio.BAÑO);
                    s1.setPrecio(150.0);
                    s1.setFechaHora(LocalDateTime.now().plusDays(1));
                    s1.setEstado("PENDIENTE");
                    s1.setObservaciones("Perro tranquilo");

                    ServicioLavado s2 = new ServicioLavado();
                    s2.setMascota(mascotas.get(1));
                    s2.setTipoServicio(TipoServicio.PELUQUERIA);
                    s2.setPrecio(350.0);
                    s2.setFechaHora(LocalDateTime.now().plusDays(2));
                    s2.setEstado("PENDIENTE");
                    s2.setObservaciones("Corte profesional");

                    ServicioLavado s3 = new ServicioLavado();
                    s3.setMascota(mascotas.get(2));
                    s3.setTipoServicio(TipoServicio.SPA);
                    s3.setPrecio(500.0);
                    s3.setFechaHora(LocalDateTime.now());
                    s3.setEstado("TERMINADO");
                    s3.setObservaciones("Spa completo aplicado");

                    List<ServicioLavado> servicios = Arrays.asList(s1, s2, s3);
                    servicioLavadoRepository.saveAll(servicios);
                    System.out.println(">>> Servicios de lavado: " + servicios.size());
                }

                if (consultaRepository.count() == 0) {
                    Consulta c1 = new Consulta();
                    c1.setMascota(mascotas.get(0));
                    c1.setVeterinario(vet);
                    c1.setSintomas("Tos seca persistente");
                    c1.setDiagnostico("Bronquitis");
                    c1.setReceta("Antibiótico por 7 días");
                    c1.setEstado(EstadoConsulta.REALIZADA);
                    
                    Consulta c2 = new Consulta();
                    c2.setMascota(mascotas.get(2));
                    c2.setVeterinario(vet);
                    c2.setSintomas("Cojera en pata derecha");
                    c2.setEstado(EstadoConsulta.PENDIENTE);

                    consultaRepository.saveAll(Arrays.asList(c1, c2));
                    System.out.println(">>> Consultas cargadas");
                }

                if (vacunaRepository.count() == 0) {
                    List<Vacuna> vacunas = Arrays.asList(
                        crearVacuna(mascotas.get(0), "Vacuna Moquillo", LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9)),
                        crearVacuna(mascotas.get(0), "Vacuna Rabia", LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(9)),
                        crearVacuna(mascotas.get(2), "Vacuna Triple Felina", LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(6)),
                        crearVacuna(mascotas.get(4), "Vacuna Polivalente", LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(11))
                    );
                    vacunaRepository.saveAll(vacunas);
                    System.out.println(">>> Vacunas cargadas: " + vacunas.size());
                }

                if (historiaClinicaRepository.count() == 0) {
                    HistoriaClinica h1 = new HistoriaClinica();
                    h1.setMascota(mascotas.get(0));
                    h1.setMotivoConsulta("Chequeo general");
                    h1.setDiagnostico("Salud estable");
                    h1.setTratamiento("Vitaminas diarias");

                    HistoriaClinica h2 = new HistoriaClinica();
                    h2.setMascota(mascotas.get(2));
                    h2.setMotivoConsulta("Control anual");
                    h2.setDiagnostico("Peso bajo");
                    h2.setTratamiento("Cambio de alimentación");

                    historiaClinicaRepository.saveAll(Arrays.asList(h1, h2));
                    System.out.println(">>> Historias clínicas cargadas");
                }
            }

            System.out.println(">>> Datos de prueba cargados correctamente!");
        };
    }

    private Mascota crearMascota(String nombre, TipoMascota tipo, String raza, Integer edad, Sexo sexo, String color, Double peso) {
        Mascota m = new Mascota();
        m.setNombre(nombre);
        m.setTipo(tipo);
        m.setRaza(raza);
        m.setEdad(edad);
        m.setSexo(sexo);
        m.setColor(color);
        m.setPeso(peso);
        m.setEstado(EstadoMascota.ACTIVA);
        return m;
    }

    private Producto crearProducto(String nombre, Double precio, Integer stock, String categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCategoria(categoria);
        return p;
    }

    private MascotaAdoptable crearMascotaAdoptable(String nombre, String especie, Integer edad, String descripcion) {
        MascotaAdoptable ma = new MascotaAdoptable();
        ma.setNombre(nombre);
        ma.setEspecie(especie);
        ma.setEdad(edad);
        ma.setDescripcion(descripcion);
        ma.setDisponible(true);
        return ma;
    }

    private Vacuna crearVacuna(Mascota mascota, String nombre, LocalDate aplicacion, LocalDate proxima) {
        Vacuna v = new Vacuna();
        v.setMascota(mascota);
        v.setNombreVacuna(nombre);
        v.setFechaAplicacion(aplicacion);
        v.setFechaProxima(proxima);
        v.setLote("LOT-" + System.currentTimeMillis());
        return v;
    }
}