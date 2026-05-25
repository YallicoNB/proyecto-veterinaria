package com.veterinaria.service;

import com.veterinaria.model.Usuario;
import com.veterinaria.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        if (passwordEncoder != null) this.passwordEncoder = passwordEncoder;
    }

    // Constructor adicional para tests y compatibilidad
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario guardarConPassword(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public List<Usuario> buscarPorRol(com.veterinaria.model.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public boolean validarCredenciales(String username, String password) {
        Optional<Usuario> opt = usuarioRepository.findByUsername(username);
        if (opt.isPresent()) {
            Usuario u = opt.get();
            Boolean activo = u.getActivo();
            if (activo == null) activo = true;
            String stored = u.getPassword();
            if (stored == null) return false;
            boolean matches;
            // Detectar si la contraseña almacenada parece BCrypt (starts with $2a$/$2b$/$2y$)
            if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                matches = passwordEncoder.matches(password, stored);
            } else {
                // Soporte retrocompatibilidad: comparar texto plano
                matches = stored.equals(password);
            }
            return matches && activo;
        }
        return false;
    }
}