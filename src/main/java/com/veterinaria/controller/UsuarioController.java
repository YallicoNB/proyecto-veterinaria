package com.veterinaria.controller;

import com.veterinaria.dto.request.LoginRequestDTO;
import com.veterinaria.dto.request.UsuarioRequest;
import com.veterinaria.dto.response.AuthResponseDTO;
import com.veterinaria.dto.response.UsuarioResponse;
import com.veterinaria.model.Usuario;
import com.veterinaria.model.Rol;
import com.veterinaria.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> response = usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(u -> ResponseEntity.ok(UsuarioResponse.fromEntity(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> buscarPorRol(@RequestParam(required = false) Rol rol) {
        List<UsuarioResponse> response;
        if (rol != null) {
            response = usuarioService.buscarPorRol(rol)
                    .stream()
                    .map(UsuarioResponse::fromEntity)
                    .collect(Collectors.toList());
        } else {
            response = usuarioService.listarTodos()
                    .stream()
                    .map(UsuarioResponse::fromEntity)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioRequest request) {
        if (usuarioService.existeUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "El usuario ya existe"));
        }
        Usuario usuario = new Usuario(request.getUsername(), request.getPassword(),
                request.getEmail(), request.getRol());
        usuario.setActivo(true);
        Usuario guardado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(UsuarioResponse.fromEntity(guardado));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        if (usuarioService.validarCredenciales(request.getUsername(), request.getPassword())) {
            Usuario usuario = usuarioService.buscarPorUsername(request.getUsername()).get();
            return ResponseEntity.ok(new AuthResponseDTO("token-temporal",
                    usuario.getUsername(), usuario.getRol().name()));
        }
        return ResponseEntity.status(401).body(java.util.Map.of("error", "Credenciales inválidas"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.buscarPorId(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setUsername(request.getUsername());
                    usuarioExistente.setEmail(request.getEmail());
                    usuarioExistente.setRol(request.getRol());
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        usuarioExistente.setPassword(request.getPassword());
                    }
                    usuarioService.guardar(usuarioExistente);
                    return ResponseEntity.ok(UsuarioResponse.fromEntity(usuarioExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (usuarioService.buscarPorId(id).isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}