package com.veterinaria.controller;

import com.veterinaria.dto.response.MascotaAdoptableResponse;
import com.veterinaria.dto.response.SolicitudAdopcionResponse;
import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.service.AdopcionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adopcion")
public class AdopcionController {

    private final AdopcionService adopcionService;

    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    @GetMapping("/disponibles")
    public List<MascotaAdoptableResponse> getDisponibles() {
        return adopcionService.listarDisponibles()
                .stream()
                .map(MascotaAdoptableResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/solicitudes")
    public SolicitudAdopcionResponse enviarSolicitud(@RequestBody SolicitudAdopcion solicitud) {
        return SolicitudAdopcionResponse.fromEntity(adopcionService.crearSolicitud(solicitud));
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public SolicitudAdopcionResponse actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return SolicitudAdopcionResponse.fromEntity(adopcionService.cambiarEstado(id, estado));
    }
}
