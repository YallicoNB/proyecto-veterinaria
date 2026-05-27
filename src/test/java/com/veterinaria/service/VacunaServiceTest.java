package com.veterinaria.service;

import com.veterinaria.model.Vacuna;
import com.veterinaria.repository.VacunaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacunaServiceTest {

    @Mock
    private VacunaRepository vacunaRepository;

    @InjectMocks
    private VacunaService vacunaService;

    @Test
    void testRegistrar() {
        Vacuna vacuna = new Vacuna();
        vacuna.setNombreVacuna("Rabia");
        when(vacunaRepository.save(vacuna)).thenReturn(vacuna);

        Vacuna resultado = vacunaService.registrar(vacuna);

        assertNotNull(resultado);
        assertEquals("Rabia", resultado.getNombreVacuna());
        verify(vacunaRepository, times(1)).save(vacuna);
    }

    @Test
    void testBuscarPorMascota() {
        Vacuna v1 = new Vacuna();
        com.veterinaria.model.Mascota m = new com.veterinaria.model.Mascota();
        m.setId(1L);
        v1.setMascota(m);
        when(vacunaRepository.findByMascotaId(1L)).thenReturn(Arrays.asList(v1));

        List<Vacuna> resultado = vacunaService.buscarPorMascota(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getMascota().getId());
    }

    @Test
    void testProximasVacunas() {
        Vacuna v1 = new Vacuna();
        v1.setFechaProxima(LocalDate.now().plusDays(10));
        when(vacunaRepository.findByFechaProximaBefore(any(LocalDate.class)))
                .thenReturn(Arrays.asList(v1));

        List<Vacuna> resultado = vacunaService.proximasVacunas();

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorId_existe() {
        Vacuna vacuna = new Vacuna();
        when(vacunaRepository.findById(1L)).thenReturn(Optional.of(vacuna));

        Optional<Vacuna> resultado = vacunaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(vacunaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Vacuna> resultado = vacunaService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testEliminar() {
        doNothing().when(vacunaRepository).deleteById(1L);

        vacunaService.eliminar(1L);

        verify(vacunaRepository, times(1)).deleteById(1L);
    }
}