package com.veterinaria.dto.response;

import com.veterinaria.model.SolicitudAdopcion;
import lombok.Data;

@Data
public class SolicitudAdopcionResponse {

    private Long id;
    private String nombreSolicitante;
    private String telefono;
    private String motivo;
    private String estado;
    private Long mascotaId;
    private String nombreMascota;

    public static SolicitudAdopcionResponse fromEntity(SolicitudAdopcion sa) {
        if (sa == null) return null;
        SolicitudAdopcionResponse dto = new SolicitudAdopcionResponse();
        dto.setId(sa.getId());
        dto.setNombreSolicitante(sa.getNombreSolicitante());
        dto.setTelefono(sa.getTelefono());
        dto.setMotivo(sa.getMotivo());
        dto.setEstado(sa.getEstado());
        if (sa.getMascota() != null) {
            dto.setMascotaId(sa.getMascota().getId());
            dto.setNombreMascota(sa.getMascota().getNombre());
        }
        return dto;
    }
}
