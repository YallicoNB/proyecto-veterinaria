package com.veterinaria.service;

import com.veterinaria.model.HistoriaClinica;
import com.veterinaria.repository.HistoriaClinicaRepository;
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
public class HistoriaClinicaServiceTest {

    @Mock
    private HistoriaClinicaRepository historiaClinicaRepository;

    @InjectMocks
    private HistoriaClinicaService historiaClinicaService;

    @Test
    void testListarTodas() {
        HistoriaClinica h1 = new HistoriaClinica();
        HistoriaClinica h2 = new HistoriaClinica();
        when(historiaClinicaRepository.findAll()).thenReturn(Arrays.asList(h1, h2));

        List<HistoriaClinica> resultado = historiaClinicaService.listarTodas();

        assertEquals(2, resultado.size());
        verify(historiaClinicaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorMascota() {
        HistoriaClinica h1 = new HistoriaClinica();
        com.veterinaria.model.Mascota m = new com.veterinaria.model.Mascota();
        m.setId(1L);
        h1.setMascota(m);
        when(historiaClinicaRepository.findByMascotaId(1L)).thenReturn(Arrays.asList(h1));

        List<HistoriaClinica> resultado = historiaClinicaService.buscarPorMascota(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getMascota().getId());
    }

    @Test
    void testGuardar() {
        HistoriaClinica historia = new HistoriaClinica();
        historia.setMotivoConsulta("No come");
        historia.setDiagnostico("Gastritis");
        when(historiaClinicaRepository.save(historia)).thenReturn(historia);

        HistoriaClinica resultado = historiaClinicaService.guardar(historia);

        assertNotNull(resultado);
        assertEquals("No come", resultado.getMotivoConsulta());
        assertEquals("Gastritis", resultado.getDiagnostico());
    }

    @Test
    void testBuscarPorId_existe() {
        HistoriaClinica historia = new HistoriaClinica();
        when(historiaClinicaRepository.findById(1L)).thenReturn(Optional.of(historia));

        Optional<HistoriaClinica> resultado = historiaClinicaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(historiaClinicaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<HistoriaClinica> resultado = historiaClinicaService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testEliminar() {
        doNothing().when(historiaClinicaRepository).deleteById(1L);

        historiaClinicaService.eliminar(1L);

        verify(historiaClinicaRepository, times(1)).deleteById(1L);
    }
}