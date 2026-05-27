package com.veterinaria.service;

import com.veterinaria.model.Rol;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.UsuarioRepository;
// Importa codificador de contraseñas Spring
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    // Mock para codificador de contraseñas
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testListarTodos() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_existe() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGuardar() {
        Usuario usuario = new Usuario("test", "pass", "test@test.com", Rol.ADMIN);
        // Simula encriptar la contraseña cruda
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);

        assertNotNull(resultado);
        assertEquals("test", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testEliminar() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExisteUsername() {
        when(usuarioRepository.existsByUsername("admin")).thenReturn(true);

        boolean resultado = usuarioService.existeUsername("admin");

        assertTrue(resultado);
        verify(usuarioRepository, times(1)).existsByUsername("admin");
    }

    @Test
    void testBuscarPorRol() {
        when(usuarioRepository.findByRol(Rol.VETERINARIO)).thenReturn(Arrays.asList(new Usuario()));

        List<Usuario> resultado = usuarioService.buscarPorRol(Rol.VETERINARIO);

        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByRol(Rol.VETERINARIO);
    }

    @Test
    void testValidarCredenciales_correctas() {
        Usuario usuario = new Usuario("admin", "pass123", "admin@test.com", Rol.ADMIN);
        usuario.setActivo(true);
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        // Simula validacion correcta de contraseña
        when(passwordEncoder.matches("pass123", "pass123")).thenReturn(true);

        boolean resultado = usuarioService.validarCredenciales("admin", "pass123");

        assertTrue(resultado);
    }

    @Test
    void testValidarCredenciales_incorrectas() {
        Usuario usuario = new Usuario("admin", "pass123", "admin@test.com", Rol.ADMIN);
        usuario.setActivo(true);
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        // Simula validacion incorrecta de contraseña
        when(passwordEncoder.matches("wrong", "pass123")).thenReturn(false);

        boolean resultado = usuarioService.validarCredenciales("admin", "wrong");

        assertFalse(resultado);
    }

    @Test
    void testValidarCredenciales_usuarioInactivo() {
        Usuario usuario = new Usuario("admin", "pass123", "admin@test.com", Rol.ADMIN);
        usuario.setActivo(false);
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        // Simula validacion para cuenta inactiva
        when(passwordEncoder.matches("pass123", "pass123")).thenReturn(true);

        boolean resultado = usuarioService.validarCredenciales("admin", "pass123");

        assertFalse(resultado);
    }
}
