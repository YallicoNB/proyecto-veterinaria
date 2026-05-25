package com.veterinaria.service;

import com.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.repository.MascotaAdoptableRepository;
import com.veterinaria.repository.SolicitudAdopcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdopcionServiceTest {

    @Mock
    private MascotaAdoptableRepository mascotaRepo;

    @Mock
    private SolicitudAdopcionRepository solicitudRepo;

    @InjectMocks
    private AdopcionService adopcionService;

    @Test
    void testListarDisponibles() {
        when(mascotaRepo.findByDisponibleTrue()).thenReturn(Arrays.asList(new MascotaAdoptable(), new MascotaAdoptable()));

        List<MascotaAdoptable> resultado = adopcionService.listarDisponibles();

        assertEquals(2, resultado.size());
        verify(mascotaRepo, times(1)).findByDisponibleTrue();
    }

    @Test
    void testCrearSolicitud_asignaPendiente() {
        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setNombreSolicitante("Juan");
        solicitud.setTelefono("123456789");
        solicitud.setMotivo("Quiero adoptar");
        MascotaAdoptable mascota = new MascotaAdoptable();
        mascota.setId(1L);
        solicitud.setMascota(mascota);

        when(solicitudRepo.save(solicitud)).thenReturn(solicitud);

        SolicitudAdopcion resultado = adopcionService.crearSolicitud(solicitud);

        assertEquals("PENDIENTE", resultado.getEstado());
        verify(solicitudRepo, times(1)).save(solicitud);
    }

    @Test
    void testCambiarEstado_aprobada_marcaMascotaNoDisponible() {
        MascotaAdoptable mascota = new MascotaAdoptable();
        mascota.setId(1L);
        mascota.setDisponible(true);

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setId(1L);
        solicitud.setMascota(mascota);
        solicitud.setEstado("PENDIENTE");

        when(solicitudRepo.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solicitudRepo.save(solicitud)).thenReturn(solicitud);
        when(mascotaRepo.save(mascota)).thenReturn(mascota);

        SolicitudAdopcion resultado = adopcionService.cambiarEstado(1L, "APROBADA");

        assertEquals("APROBADA", resultado.getEstado());
        assertFalse(mascota.getDisponible());
        verify(mascotaRepo, times(1)).save(mascota);
    }

    @Test
    void testCambiarEstado_rechazada_noCambiaDisponible() {
        MascotaAdoptable mascota = new MascotaAdoptable();
        mascota.setId(1L);
        mascota.setDisponible(true);

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setId(1L);
        solicitud.setMascota(mascota);

        when(solicitudRepo.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solicitudRepo.save(solicitud)).thenReturn(solicitud);

        SolicitudAdopcion resultado = adopcionService.cambiarEstado(1L, "RECHAZADA");

        assertEquals("RECHAZADA", resultado.getEstado());
        assertTrue(mascota.getDisponible());
        verify(mascotaRepo, never()).save(any());
    }

    @Test
    void testCambiarEstado_noExiste_lanzaExcepcion() {
        when(solicitudRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adopcionService.cambiarEstado(99L, "APROBADA"));
    }
}
