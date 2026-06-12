package com.veterinaria.dto.response;

import com.veterinaria.model.Vacuna;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VacunaResponse {

    private Long id;
    private Long mascotaId;
    private String nombreMascota;
    private String nombreVacuna;
    private LocalDate fechaAplicacion;
    private LocalDate fechaProxima;
    private String lote;

    public static VacunaResponse fromEntity(Vacuna v) {
        if (v == null) return null;
        VacunaResponse dto = new VacunaResponse();
        dto.setId(v.getId());
        if (v.getMascota() != null) {
            dto.setMascotaId(v.getMascota().getId());
            dto.setNombreMascota(v.getMascota().getNombre());
        }
        dto.setNombreVacuna(v.getNombreVacuna());
        dto.setFechaAplicacion(v.getFechaAplicacion());
        dto.setFechaProxima(v.getFechaProxima());
        dto.setLote(v.getLote());
        return dto;
    }
}
