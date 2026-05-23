package com.veterinaria.controller;

import com.veterinaria.model.Usuario;
import com.veterinaria.model.Rol;
import com.veterinaria.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorRol(@RequestParam(required = false) Rol rol) {
        if (rol != null) {
            return ResponseEntity.ok(usuarioService.buscarPorRol(rol));
        }
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        if (usuarioService.existeUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        usuario.setActivo(true);
        Usuario guardado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(guardado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (usuarioService.validarCredenciales(request.getUsername(), request.getPassword())) {
            Usuario usuario = usuarioService.buscarPorUsername(request.getUsername()).get();
            return ResponseEntity.ok(new AuthResponse("token-temporal", usuario.getUsername(), usuario.getRol().name()));
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.buscarPorId(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setUsername(usuarioActualizado.getUsername());
                    usuarioExistente.setEmail(usuarioActualizado.getEmail());
                    usuarioExistente.setRol(usuarioActualizado.getRol());
                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                        usuarioExistente.setPassword(usuarioActualizado.getPassword());
                    }
                    usuarioService.guardar(usuarioExistente);
                    return ResponseEntity.ok(usuarioExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (usuarioService.buscarPorId(id).isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario eliminado");
        }
        return ResponseEntity.notFound().build();
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthResponse {
    private String token;
    private String username;
    private String rol;

    public AuthResponse(String token, String username, String rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRol() { return rol; }
}