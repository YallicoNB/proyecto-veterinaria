package com.veterinaria.service;

import com.veterinaria.exception.BusinessException;
import com.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.ServicioLavado;
import com.veterinaria.model.TipoServicio;
import com.veterinaria.repository.ServicioLavadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LavanderiaServiceTest {

    @Mock
    private ServicioLavadoRepository servicioLavadoRepository;

    @InjectMocks
    private LavanderiaService lavanderiaService;

    @Test
    void testCrearServicio_asignaPendiente() {
        ServicioLavado servicio = new ServicioLavado();
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        servicio.setMascota(mascota);
        servicio.setTipoServicio(TipoServicio.BAÑO);
        servicio.setPrecio(150.0);
        servicio.setFechaHora(LocalDateTime.now());

        when(servicioLavadoRepository.save(servicio)).thenReturn(servicio);

        ServicioLavado resultado = lavanderiaService.crearServicio(servicio);

        assertEquals("PENDIENTE", resultado.getEstado());
        verify(servicioLavadoRepository, times(1)).save(servicio);
    }

    @Test
    void testListarTodos() {
        when(servicioLavadoRepository.findAll()).thenReturn(Arrays.asList(new ServicioLavado(), new ServicioLavado()));

        List<ServicioLavado> resultado = lavanderiaService.listarTodos();

        assertEquals(2, resultado.size());
        verify(servicioLavadoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_existe() {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setId(1L);
        when(servicioLavadoRepository.findById(1L)).thenReturn(Optional.of(servicio));

        ServicioLavado resultado = lavanderiaService.obtenerPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerPorId_noExiste() {
        when(servicioLavadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lavanderiaService.obtenerPorId(99L));
    }

    @Test
    void testCambiarEstado_exito() {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setId(1L);
        servicio.setEstado("PENDIENTE");
        when(servicioLavadoRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(servicioLavadoRepository.save(servicio)).thenReturn(servicio);

        ServicioLavado resultado = lavanderiaService.cambiarEstado(1L, "EN_PROCESO");

        assertEquals("EN_PROCESO", resultado.getEstado());
    }

    @Test
    void testCambiarEstado_estadoInvalido_lanzaExcepcion() {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setId(1L);
        when(servicioLavadoRepository.findById(1L)).thenReturn(Optional.of(servicio));

        assertThrows(BusinessException.class, () -> lavanderiaService.cambiarEstado(1L, "INEXISTENTE"));
    }

    @Test
    void testCambiarEstado_noExiste_lanzaExcepcion() {
        when(servicioLavadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lavanderiaService.cambiarEstado(99L, "PENDIENTE"));
    }

    @Test
    void testListarPendientes() {
        when(servicioLavadoRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(new ServicioLavado()));

        List<ServicioLavado> resultado = lavanderiaService.listarPendientes();

        assertEquals(1, resultado.size());
        verify(servicioLavadoRepository, times(1)).findByEstado("PENDIENTE");
    }
}
