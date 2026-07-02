package com.veterinaria.service;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import com.veterinaria.model.HistoriaClinica;
import com.veterinaria.repository.ConsultaRepository;
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
public class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private HistoriaClinicaService historiaClinicaService;

    @InjectMocks
    private ConsultaService consultaService;

    @Test
    void testListarTodas() {
        // preparamos datos falsos
        Consulta c1 = new Consulta();
        Consulta c2 = new Consulta();
        when(consultaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // ejecutamos el método
        List<Consulta> resultado = consultaService.listarTodas();

        // verificamos
        assertEquals(2, resultado.size());
        verify(consultaRepository, times(1)).findAll();
    }

    @Test
    void testAgendar() {
        Consulta consulta = new Consulta();
        consulta.setSintomas("Tos");
        when(consultaRepository.save(consulta)).thenReturn(consulta);

        Consulta resultado = consultaService.agendar(consulta);

        assertNotNull(resultado);
        assertEquals("Tos", resultado.getSintomas());
        verify(consultaRepository, times(1)).save(consulta);
    }

    @Test
    void testAtender_existeConsulta() {
        Consulta consulta = new Consulta();
        consulta.setEstado(EstadoConsulta.PENDIENTE);
        consulta.setSintomas("Tos persistente");

        Consulta datos = new Consulta();
        datos.setDiagnostico("Infección");
        datos.setReceta("Antibiótico");

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(consulta)).thenReturn(consulta);
        when(historiaClinicaService.guardar(any(HistoriaClinica.class))).thenReturn(new HistoriaClinica());

        Optional<Consulta> resultado = consultaService.atender(1L, datos);

        assertTrue(resultado.isPresent());
        assertEquals(EstadoConsulta.REALIZADA, resultado.get().getEstado());
        assertEquals("Infección", resultado.get().getDiagnostico());
    }

    @Test
    void testAtender_noExisteConsulta() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Consulta> resultado = consultaService.atender(99L, new Consulta());

        assertFalse(resultado.isPresent());
    }

    @Test
    void testCancelar_existeConsulta() {
        Consulta consulta = new Consulta();
        consulta.setEstado(EstadoConsulta.PENDIENTE);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(consulta)).thenReturn(consulta);

        Optional<Consulta> resultado = consultaService.cancelar(1L);

        assertTrue(resultado.isPresent());
        assertEquals(EstadoConsulta.CANCELADA, resultado.get().getEstado());
    }

    @Test
    void testCancelar_noExisteConsulta() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Consulta> resultado = consultaService.cancelar(99L);

        assertFalse(resultado.isPresent());
    }
}