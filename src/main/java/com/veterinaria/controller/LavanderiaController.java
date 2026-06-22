package com.veterinaria.controller;

import com.veterinaria.dto.request.ServicioLavadoRequest;
import com.veterinaria.dto.response.ServicioLavadoResponse;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.ServicioLavado;
import com.veterinaria.service.LavanderiaService;
import com.veterinaria.service.MascotaService;
import jakarta.validation.Valid;
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
    private final MascotaService mascotaService;

    public LavanderiaController(LavanderiaService lavanderiaService, MascotaService mascotaService) {
        this.lavanderiaService = lavanderiaService;
        this.mascotaService = mascotaService;
    }

    @PostMapping
    public ResponseEntity<ServicioLavadoResponse> crearServicio(@Valid @RequestBody ServicioLavadoRequest request) {
        ServicioLavado entity = new ServicioLavado();
        entity.setMascota(mascotaService.buscarPorId(request.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada")));
        entity.setTipoServicio(request.getTipoServicio());
        entity.setPrecio(request.getPrecio());
        entity.setFechaHora(request.getFechaHora());
        entity.setObservaciones(request.getObservaciones());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicioLavadoResponse.fromEntity(lavanderiaService.crearServicio(entity)));
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
