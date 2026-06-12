package com.veterinaria.dto.response;

import com.veterinaria.model.HistoriaClinica;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoriaClinicaResponse {

    private Long id;
    private Long mascotaId;
    private String nombreMascota;
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;
    private LocalDateTime fechaCreacion;

    public static HistoriaClinicaResponse fromEntity(HistoriaClinica hc) {
        if (hc == null) return null;
        HistoriaClinicaResponse dto = new HistoriaClinicaResponse();
        dto.setId(hc.getId());
        if (hc.getMascota() != null) {
            dto.setMascotaId(hc.getMascota().getId());
            dto.setNombreMascota(hc.getMascota().getNombre());
        }
        dto.setMotivoConsulta(hc.getMotivoConsulta());
        dto.setDiagnostico(hc.getDiagnostico());
        dto.setTratamiento(hc.getTratamiento());
        dto.setFechaCreacion(hc.getFechaCreacion());
        return dto;
    }
}
