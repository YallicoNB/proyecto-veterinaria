package com.veterinaria.controller;

import com.veterinaria.model.*;
import com.veterinaria.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/veterinaria")
public class VeterinariaController {

    private final HistoriaClinicaService historiaClinicaService;
    private final ConsultaService consultaService;
    private final VacunaService vacunaService;

    public VeterinariaController(HistoriaClinicaService historiaClinicaService,
            ConsultaService consultaService,
            VacunaService vacunaService) {
        this.historiaClinicaService = historiaClinicaService;
        this.consultaService = consultaService;
        this.vacunaService = vacunaService;
    }

    // ── Historia Clínica ──────────────────────────────────────────

    @GetMapping("/historia/{idMascota}")
    public ResponseEntity<List<HistoriaClinica>> obtenerHistoria(@PathVariable Long idMascota) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorMascota(idMascota));
    }

    @PostMapping("/historia")
    public ResponseEntity<HistoriaClinica> crearHistoria(@RequestBody HistoriaClinica historia) {
        return ResponseEntity.ok(historiaClinicaService.guardar(historia));
    }

    @DeleteMapping("/historia/{id}")
    public ResponseEntity<?> eliminarHistoria(@PathVariable Long id) {
        if (historiaClinicaService.buscarPorId(id).isPresent()) {
            historiaClinicaService.eliminar(id);
            return ResponseEntity.ok("Historia clínica eliminada");
        }
        return ResponseEntity.notFound().build();
    }

    // ── Consultas ─────────────────────────────────────────────────

    @PostMapping("/consulta")
    public ResponseEntity<Consulta> agendarConsulta(@RequestBody Consulta consulta) {
        return ResponseEntity.ok(consultaService.agendar(consulta));
    }

    @GetMapping("/consulta/mascota/{id}")
    public ResponseEntity<List<Consulta>> consultasPorMascota(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorMascota(id));
    }

    @GetMapping("/consulta")
    public ResponseEntity<List<Consulta>> listarConsultas(
            @RequestParam(required = false) EstadoConsulta estado) {
        if (estado != null) {
            return ResponseEntity.ok(consultaService.buscarPorEstado(estado));
        }
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @PutMapping("/consulta/{id}/atender")
    public ResponseEntity<?> atenderConsulta(@PathVariable Long id, @RequestBody Consulta datos) {
        return consultaService.atender(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/consulta/{id}/cancelar")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
        return consultaService.cancelar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Vacunas ───────────────────────────────────────────────────

    @PostMapping("/vacuna")
    public ResponseEntity<Vacuna> registrarVacuna(@RequestBody Vacuna vacuna) {
        return ResponseEntity.ok(vacunaService.registrar(vacuna));
    }

    @GetMapping("/vacuna/mascota/{id}")
    public ResponseEntity<List<Vacuna>> vacunasPorMascota(@PathVariable Long id) {
        return ResponseEntity.ok(vacunaService.buscarPorMascota(id));
    }

    @GetMapping("/vacuna/proximas")
    public ResponseEntity<List<Vacuna>> proximasVacunas() {
        return ResponseEntity.ok(vacunaService.proximasVacunas());
    }

    @DeleteMapping("/vacuna/{id}")
    public ResponseEntity<?> eliminarVacuna(@PathVariable Long id) {
        if (vacunaService.buscarPorId(id).isPresent()) {
            vacunaService.eliminar(id);
            return ResponseEntity.ok("Vacuna eliminada");
        }
        return ResponseEntity.notFound().build();
    }
}