package com.veterinaria.controller;

import com.veterinaria.model.Rol;
import com.veterinaria.model.Usuario;
import com.veterinaria.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        Usuario usuario = new Usuario("test", "pass", "test@test.com", Rol.ADMIN);
        usuario.setId(1L);
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorRol_debeRetornar200() throws Exception {
        when(usuarioService.buscarPorRol(Rol.VETERINARIO)).thenReturn(List.of(new Usuario()));

        mockMvc.perform(get("/api/usuarios/buscar").param("rol", "VETERINARIO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void registro_cuandoUsuarioNoExiste_debeRetornar200() throws Exception {
        Usuario usuario = new Usuario("nuevo", "pass", "nuevo@test.com", Rol.CLIENTE_TIENDA);
        when(usuarioService.existeUsername("nuevo")).thenReturn(false);
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuario);

        String json = """
                {
                    "username": "nuevo",
                    "password": "pass",
                    "email": "nuevo@test.com",
                    "rol": "CLIENTE_TIENDA"
                }
                """;

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nuevo"));
    }

    @Test
    void registro_cuandoUsuarioYaExiste_debeRetornar400() throws Exception {
        when(usuarioService.existeUsername("nuevo")).thenReturn(true);

        String json = """
                {
                    "username": "nuevo",
                    "password": "pass",
                    "email": "nuevo@test.com",
                    "rol": "CLIENTE_TIENDA"
                }
                """;

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_conCredencialesCorrectas_debeRetornar200() throws Exception {
        when(usuarioService.validarCredenciales("admin", "admin123")).thenReturn(true);
        Usuario usuario = new Usuario("admin", "admin123", "admin@test.com", Rol.ADMIN);
        when(usuarioService.buscarPorUsername("admin")).thenReturn(Optional.of(usuario));

        String json = """
                {
                    "username": "admin",
                    "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void login_conCredencialesInvalidas_debeRetornar401() throws Exception {
        when(usuarioService.validarCredenciales("admin", "wrong")).thenReturn(false);

        String json = """
                {
                    "username": "admin",
                    "password": "wrong"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void eliminar_cuandoExiste_debeRetornar204() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(new Usuario()));

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_debeRetornar404() throws Exception {
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
