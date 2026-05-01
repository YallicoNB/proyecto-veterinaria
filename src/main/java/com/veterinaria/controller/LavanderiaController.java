package com.veterinaria.controller;

import com.veterinaria.model.ServicioLavado;
import com.veterinaria.service.LavanderiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lavanderia/servicio")
public class LavanderiaController {

    @Autowired
    private LavanderiaService lavanderiaService;

    @PostMapping
    public ResponseEntity<ServicioLavado> crearServicio(@RequestBody ServicioLavado servicio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lavanderiaService.crearServicio(servicio));
    }

    @GetMapping
    public ResponseEntity<List<ServicioLavado>> listarTodos() {
        return ResponseEntity.ok(lavanderiaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioLavado> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(lavanderiaService.obtenerPorId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ServicioLavado> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(lavanderiaService.cambiarEstado(id, body.get("estado")));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<ServicioLavado>> listarPendientes() {
        return ResponseEntity.ok(lavanderiaService.listarPendientes());
    }
}