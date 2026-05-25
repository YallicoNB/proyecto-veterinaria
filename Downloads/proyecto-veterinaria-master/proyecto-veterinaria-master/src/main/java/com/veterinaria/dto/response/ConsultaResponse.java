package com.veterinaria.dto.response;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaResponse {

    private Long id;
    private Long mascotaId;
    private String nombreMascota;
    private Long veterinarioId;
    private String nombreVeterinario;
    private String sintomas;
    private String diagnostico;
    private String receta;
    private EstadoConsulta estado;
    private LocalDateTime fecha;

    public static ConsultaResponse fromEntity(Consulta consulta) {
        ConsultaResponse dto = new ConsultaResponse();
        dto.setId(consulta.getId());
        if (consulta.getMascota() != null) {
            dto.setMascotaId(consulta.getMascota().getId());
            dto.setNombreMascota(consulta.getMascota().getNombre());
        }
        if (consulta.getVeterinario() != null) {
            dto.setVeterinarioId(consulta.getVeterinario().getId());
            dto.setNombreVeterinario(consulta.getVeterinario().getUsername());
        }
        dto.setSintomas(consulta.getSintomas());
        dto.setDiagnostico(consulta.getDiagnostico());
        dto.setReceta(consulta.getReceta());
        dto.setEstado(consulta.getEstado());
        dto.setFecha(consulta.getFecha());
        return dto;
    }
}
