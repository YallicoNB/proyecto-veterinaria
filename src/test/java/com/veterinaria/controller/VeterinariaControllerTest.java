package com.veterinaria.controller;

import com.veterinaria.model.*;
import com.veterinaria.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeterinariaController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.ActiveProfiles("test")
class VeterinariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriaClinicaService historiaClinicaService;

    @MockBean
    private ConsultaService consultaService;

    @MockBean
    private VacunaService vacunaService;

    @MockBean
    private MascotaService mascotaService;

    @Test
    void obtenerHistoria_debeRetornar200() throws Exception {
        when(historiaClinicaService.buscarPorMascota(1L)).thenReturn(List.of(new HistoriaClinica()));

        mockMvc.perform(get("/api/veterinaria/historia/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void agendarConsulta_debeRetornar200() throws Exception {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        Consulta consulta = new Consulta();
        consulta.setSintomas("Tos");
        consulta.setMascota(mascota);

        when(mascotaService.buscarPorId(1L)).thenReturn(Optional.of(mascota));
        when(consultaService.agendar(any(Consulta.class))).thenReturn(consulta);

        String json = """
                {
                    "mascotaId": 1,
                    "sintomas": "Tos"
                }
                """;

        mockMvc.perform(post("/api/veterinaria/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sintomas").value("Tos"));
    }

    @Test
    void consultasPorMascota_debeRetornar200() throws Exception {
        when(consultaService.buscarPorMascota(1L)).thenReturn(List.of(new Consulta()));

        mockMvc.perform(get("/api/veterinaria/consulta/mascota/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void listarConsultas_debeRetornar200() throws Exception {
        when(consultaService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/veterinaria/consulta"))
                .andExpect(status().isOk());
    }

    @Test
    void atenderConsulta_debeRetornar200() throws Exception {
        Consulta atendida = new Consulta();
        atendida.setDiagnostico("Infección");
        atendida.setReceta("Antibiótico");
        atendida.setEstado(EstadoConsulta.REALIZADA);

        when(consultaService.atender(any(Long.class), any(Consulta.class)))
                .thenReturn(Optional.of(atendida));

        String json = """
                {
                    "diagnostico": "Infección",
                    "receta": "Antibiótico"
                }
                """;

        mockMvc.perform(put("/api/veterinaria/consulta/1/atender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("REALIZADA"));
    }

    @Test
    void cancelarConsulta_debeRetornar200() throws Exception {
        Consulta cancelada = new Consulta();
        cancelada.setEstado(EstadoConsulta.CANCELADA);
        when(consultaService.cancelar(1L)).thenReturn(Optional.of(cancelada));

        mockMvc.perform(put("/api/veterinaria/consulta/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }

    @Test
    void registrarVacuna_debeRetornar200() throws Exception {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        Vacuna vacuna = new Vacuna();
        vacuna.setNombreVacuna("Rabia");
        vacuna.setMascota(mascota);

        when(mascotaService.buscarPorId(1L)).thenReturn(Optional.of(mascota));
        when(vacunaService.registrar(any(Vacuna.class))).thenReturn(vacuna);

        String json = """
                {
                    "mascotaId": 1,
                    "nombreVacuna": "Rabia"
                }
                """;

        mockMvc.perform(post("/api/veterinaria/vacuna")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreVacuna").value("Rabia"));
    }

    @Test
    void vacunasPorMascota_debeRetornar200() throws Exception {
        when(vacunaService.buscarPorMascota(1L)).thenReturn(List.of(new Vacuna()));

        mockMvc.perform(get("/api/veterinaria/vacuna/mascota/1"))
                .andExpect(status().isOk());
    }

    @Test
    void proximasVacunas_debeRetornar200() throws Exception {
        when(vacunaService.proximasVacunas()).thenReturn(List.of());

        mockMvc.perform(get("/api/veterinaria/vacuna/proximas"))
                .andExpect(status().isOk());
    }
}
