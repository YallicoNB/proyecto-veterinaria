package com.veterinaria.controller;

import com.veterinaria.dto.request.SolicitudAdopcionRequest;
import com.veterinaria.dto.response.MascotaAdoptableResponse;
import com.veterinaria.dto.response.SolicitudAdopcionResponse;
import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.service.AdopcionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adopcion")
public class AdopcionController {

    private final AdopcionService adopcionService;
    private final com.veterinaria.service.MascotaAdoptableService mascotaAdoptableService;

    public AdopcionController(AdopcionService adopcionService,
            com.veterinaria.service.MascotaAdoptableService mascotaAdoptableService) {
        this.adopcionService = adopcionService;
        this.mascotaAdoptableService = mascotaAdoptableService;
    }

    @GetMapping("/disponibles")
    public List<MascotaAdoptableResponse> getDisponibles() {
        return adopcionService.listarDisponibles()
                .stream()
                .map(MascotaAdoptableResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/solicitudes")
    public SolicitudAdopcionResponse enviarSolicitud(@Valid @RequestBody SolicitudAdopcionRequest request) {
        MascotaAdoptable mascota = mascotaAdoptableService.buscarPorId(request.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        SolicitudAdopcion entity = new SolicitudAdopcion();
        entity.setMascota(mascota);
        entity.setNombreSolicitante(request.getNombreSolicitante());
        entity.setTelefono(request.getTelefono());
        entity.setMotivo(request.getMotivo());
        return SolicitudAdopcionResponse.fromEntity(adopcionService.crearSolicitud(entity));
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public SolicitudAdopcionResponse actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return SolicitudAdopcionResponse.fromEntity(adopcionService.cambiarEstado(id, estado));
    }
}
