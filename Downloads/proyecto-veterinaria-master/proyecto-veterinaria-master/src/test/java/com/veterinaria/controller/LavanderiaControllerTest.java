package com.veterinaria.controller;

import com.veterinaria.model.Mascota;
import com.veterinaria.model.ServicioLavado;
import com.veterinaria.model.TipoServicio;
import com.veterinaria.service.LavanderiaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LavanderiaController.class)
@AutoConfigureMockMvc(addFilters = false)
class LavanderiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LavanderiaService lavanderiaService;

    @Test
    void crearServicio_debeRetornar201() throws Exception {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setTipoServicio(TipoServicio.BAÑO);
        servicio.setPrecio(150.0);
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        servicio.setMascota(mascota);

        when(lavanderiaService.crearServicio(any(ServicioLavado.class))).thenReturn(servicio);

        String json = """
                {
                    "tipoServicio": "BAÑO",
                    "precio": 150.0,
                    "fechaHora": "2026-06-01T10:00:00",
                    "mascota": {"id": 1}
                }
                """;

        mockMvc.perform(post("/api/lavanderia/servicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(lavanderiaService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/lavanderia/servicio"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void cambiarEstado_conPatch_debeRetornar200() throws Exception {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setEstado("EN_PROCESO");
        when(lavanderiaService.cambiarEstado(anyLong(), anyString())).thenReturn(servicio);

        String json = """
                {
                    "estado": "EN_PROCESO"
                }
                """;

        mockMvc.perform(patch("/api/lavanderia/servicio/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    void listarPendientes_debeRetornar200() throws Exception {
        when(lavanderiaService.listarPendientes()).thenReturn(List.of(new ServicioLavado()));

        mockMvc.perform(get("/api/lavanderia/servicio/pendientes"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_debeRetornar200() throws Exception {
        ServicioLavado servicio = new ServicioLavado();
        servicio.setId(1L);
        when(lavanderiaService.obtenerPorId(1L)).thenReturn(servicio);

        mockMvc.perform(get("/api/lavanderia/servicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
