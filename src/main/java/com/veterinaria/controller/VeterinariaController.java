package com.veterinaria.controller;

import com.veterinaria.dto.response.ConsultaResponse;
import com.veterinaria.dto.response.HistoriaClinicaResponse;
import com.veterinaria.dto.response.VacunaResponse;
import com.veterinaria.model.*;
import com.veterinaria.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<HistoriaClinicaResponse>> obtenerHistoria(@PathVariable Long idMascota) {
        List<HistoriaClinicaResponse> response = historiaClinicaService.buscarPorMascota(idMascota)
                .stream()
                .map(HistoriaClinicaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/historia")
    public ResponseEntity<HistoriaClinicaResponse> crearHistoria(@Valid @RequestBody HistoriaClinica historia) {
        return ResponseEntity.ok(HistoriaClinicaResponse.fromEntity(historiaClinicaService.guardar(historia)));
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
    public ResponseEntity<ConsultaResponse> agendarConsulta(@Valid @RequestBody Consulta consulta) {
        return ResponseEntity.ok(ConsultaResponse.fromEntity(consultaService.agendar(consulta)));
    }

    @GetMapping("/consulta/mascota/{id}")
    public ResponseEntity<List<ConsultaResponse>> consultasPorMascota(@PathVariable Long id) {
        List<ConsultaResponse> response = consultaService.buscarPorMascota(id)
                .stream()
                .map(ConsultaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consulta")
    public ResponseEntity<List<ConsultaResponse>> listarConsultas(
            @RequestParam(required = false) EstadoConsulta estado) {
        List<Consulta> consultas = (estado != null)
                ? consultaService.buscarPorEstado(estado)
                : consultaService.listarTodas();
        List<ConsultaResponse> response = consultas.stream()
                .map(ConsultaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/consulta/{id}/atender")
    public ResponseEntity<?> atenderConsulta(@PathVariable Long id, @Valid @RequestBody Consulta datos) {
        return consultaService.atender(id, datos)
                .map(c -> ResponseEntity.ok(ConsultaResponse.fromEntity(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/consulta/{id}/cancelar")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
        return consultaService.cancelar(id)
                .map(c -> ResponseEntity.ok(ConsultaResponse.fromEntity(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Vacunas ───────────────────────────────────────────────────

    @PostMapping("/vacuna")
    public ResponseEntity<VacunaResponse> registrarVacuna(@Valid @RequestBody Vacuna vacuna) {
        return ResponseEntity.ok(VacunaResponse.fromEntity(vacunaService.registrar(vacuna)));
    }

    @GetMapping("/vacuna/mascota/{id}")
    public ResponseEntity<List<VacunaResponse>> vacunasPorMascota(@PathVariable Long id) {
        List<VacunaResponse> response = vacunaService.buscarPorMascota(id)
                .stream()
                .map(VacunaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vacuna/proximas")
    public ResponseEntity<List<VacunaResponse>> proximasVacunas() {
        List<VacunaResponse> response = vacunaService.proximasVacunas()
                .stream()
                .map(VacunaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
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