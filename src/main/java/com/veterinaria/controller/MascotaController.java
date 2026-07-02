package com.veterinaria.controller;

import com.veterinaria.dto.request.MascotaRequest;
import com.veterinaria.dto.response.MascotaResponse;
import com.veterinaria.model.Mascota;
import com.veterinaria.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veterinaria/mascota")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponse>> listarTodas() {
        List<MascotaResponse> response = mascotaService.listarTodos()
                .stream()
                .map(MascotaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponse> obtenerPorId(@PathVariable Long id) {
        return mascotaService.buscarPorId(id)
                .map(m -> ResponseEntity.ok(MascotaResponse.fromEntity(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MascotaResponse> crear(@Valid @RequestBody MascotaRequest request) {
        Mascota entity = new Mascota();
        entity.setNombre(request.getNombre());
        entity.setTipo(request.getTipo());
        entity.setRaza(request.getRaza());
        entity.setEdad(request.getEdad());
        entity.setSexo(request.getSexo());
        entity.setColor(request.getColor());
        entity.setPeso(request.getPeso());
        entity.setObservaciones(request.getObservaciones());
        entity.setFotoUrl(request.getFotoUrl());
        return ResponseEntity.ok(MascotaResponse.fromEntity(mascotaService.guardar(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody MascotaRequest request) {
        return mascotaService.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(request.getNombre());
                    existente.setTipo(request.getTipo());
                    existente.setRaza(request.getRaza());
                    existente.setEdad(request.getEdad());
                    existente.setSexo(request.getSexo());
                    existente.setColor(request.getColor());
                    existente.setPeso(request.getPeso());
                    existente.setObservaciones(request.getObservaciones());
                    existente.setFotoUrl(request.getFotoUrl());
                    return ResponseEntity.ok(MascotaResponse.fromEntity(mascotaService.actualizar(existente)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (mascotaService.buscarPorId(id).isPresent()) {
            mascotaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
