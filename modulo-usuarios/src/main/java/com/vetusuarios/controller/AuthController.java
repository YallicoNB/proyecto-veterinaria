package com.vetusuarios.controller;

import com.vetcommon.model.Rol;
import com.vetcommon.model.Usuario;
import com.vetcommon.repository.UsuarioRepository;
import com.vetusuarios.service.AuthService;
import com.vetusuarios.dto.LoginRequest;
import com.vetusuarios.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (authService.validarCredenciales(request.getUsername(), request.getPassword())) {
            Usuario usuario = authService.findByUsername(request.getUsername()).get();
            return ResponseEntity.ok(new AuthResponse("token-temporal", usuario.getUsername(), usuario.getRol().name()));
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        if (authService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        usuario.setActivo(true);
        Usuario guardado = authService.guardar(usuario);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Módulo Usuarios funcionando!");
    }

    @GetMapping("/crear")
    public ResponseEntity<?> crearTemporal(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String rol) {
        if (authService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        Usuario nuevo = new Usuario(username, password, email, Rol.valueOf(rol));
        nuevo.setActivo(true);
        Usuario guardado = authService.guardar(nuevo);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/login-test")
    public ResponseEntity<?> loginTemporal(
            @RequestParam String username,
            @RequestParam String password) {
        if (authService.validarCredenciales(username, password)) {
            Usuario usuario = authService.findByUsername(username).get();
            return ResponseEntity.ok(new AuthResponse("token-temporal", usuario.getUsername(), usuario.getRol().name()));
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorRol(@RequestParam(required = false) Rol rol) {
        if (rol != null) {
            return ResponseEntity.ok(usuarioRepository.findByRol(rol));
        }
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setUsername(usuarioActualizado.getUsername());
                    usuarioExistente.setEmail(usuarioActualizado.getEmail());
                    usuarioExistente.setRol(usuarioActualizado.getRol());
                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                        usuarioExistente.setPassword(usuarioActualizado.getPassword());
                    }
                    usuarioRepository.save(usuarioExistente);
                    return ResponseEntity.ok(usuarioExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado");
        }
        return ResponseEntity.notFound().build();
    }
}