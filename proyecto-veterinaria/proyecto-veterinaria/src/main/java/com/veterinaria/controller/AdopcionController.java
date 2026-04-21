package com.veterinaria.controller;

import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.service.AdopcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/adopcion")
public class AdopcionController {

    @Autowired
    private AdopcionService adopcionService;

    @GetMapping("/disponibles")
    public List<MascotaAdoptable> getDisponibles() {
        return adopcionService.listarDisponibles();
    }

    @PostMapping("/solicitudes")
    public SolicitudAdopcion enviarSolicitud(@RequestBody SolicitudAdopcion solicitud) {
        return adopcionService.crearSolicitud(solicitud);
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public SolicitudAdopcion actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return adopcionService.cambiarEstado(id, estado);
    }
}
