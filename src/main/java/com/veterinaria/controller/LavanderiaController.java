package com.veterinaria.controller;

import com.veterinaria.dto.response.ServicioLavadoResponse;
import com.veterinaria.model.ServicioLavado;
import com.veterinaria.service.LavanderiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lavanderia/servicio")
public class LavanderiaController {

    private final LavanderiaService lavanderiaService;

    public LavanderiaController(LavanderiaService lavanderiaService) {
        this.lavanderiaService = lavanderiaService;
    }

    @PostMapping
    public ResponseEntity<ServicioLavadoResponse> crearServicio(@RequestBody ServicioLavado servicio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicioLavadoResponse.fromEntity(lavanderiaService.crearServicio(servicio)));
    }

    @GetMapping
    public ResponseEntity<List<ServicioLavadoResponse>> listarTodos() {
        List<ServicioLavadoResponse> response = lavanderiaService.listarTodos()
                .stream()
                .map(ServicioLavadoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioLavadoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ServicioLavadoResponse.fromEntity(lavanderiaService.obtenerPorId(id)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServicioLavadoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ServicioLavadoResponse.fromEntity(
                lavanderiaService.cambiarEstado(id, body.get("estado"))));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<ServicioLavadoResponse>> listarPendientes() {
        List<ServicioLavadoResponse> response = lavanderiaService.listarPendientes()
                .stream()
                .map(ServicioLavadoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}