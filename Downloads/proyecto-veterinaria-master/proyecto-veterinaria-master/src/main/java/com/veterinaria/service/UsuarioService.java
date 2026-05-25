package com.veterinaria.service;

import com.veterinaria.model.Usuario;
import com.veterinaria.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

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
            return u.getPassword().equals(password) && activo;
        }
        return false;
    }
}