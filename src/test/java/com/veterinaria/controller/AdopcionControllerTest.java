package com.veterinaria.controller;

import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.service.AdopcionService;
import com.veterinaria.service.MascotaAdoptableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdopcionController.class)
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.ActiveProfiles("test")
class AdopcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdopcionService adopcionService;

    @MockBean
    private MascotaAdoptableService mascotaAdoptableService;

    @Test
    void getDisponibles_debeRetornar200() throws Exception {
        when(adopcionService.listarDisponibles()).thenReturn(List.of(new MascotaAdoptable()));

        mockMvc.perform(get("/api/adopcion/disponibles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void enviarSolicitud_debeRetornar200() throws Exception {
        MascotaAdoptable mascota = new MascotaAdoptable();
        mascota.setId(1L);
        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setNombreSolicitante("Juan");
        solicitud.setMascota(mascota);

        when(mascotaAdoptableService.buscarPorId(1L)).thenReturn(Optional.of(mascota));
        when(adopcionService.crearSolicitud(any(SolicitudAdopcion.class))).thenReturn(solicitud);

        String json = """
                {
                    "mascotaId": 1,
                    "nombreSolicitante": "Juan",
                    "telefono": "123456789",
                    "motivo": "Quiero adoptar"
                }
                """;

        mockMvc.perform(post("/api/adopcion/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreSolicitante").value("Juan"));
    }

    @Test
    void actualizarEstado_debeRetornar200() throws Exception {
        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setEstado("APROBADA");
        when(adopcionService.cambiarEstado(anyLong(), anyString())).thenReturn(solicitud);

        mockMvc.perform(patch("/api/adopcion/solicitudes/1/estado")
                        .param("estado", "APROBADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));
    }
}
