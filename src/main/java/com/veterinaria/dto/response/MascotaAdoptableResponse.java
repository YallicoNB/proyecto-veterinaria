package com.veterinaria.dto.response;

import com.veterinaria.model.MascotaAdoptable;
import lombok.Data;

@Data
public class MascotaAdoptableResponse {

    private Long id;
    private String nombre;
    private String especie;
    private Integer edad;
    private String descripcion;
    private Boolean disponible;

    public static MascotaAdoptableResponse fromEntity(MascotaAdoptable ma) {
        if (ma == null) return null;
        MascotaAdoptableResponse dto = new MascotaAdoptableResponse();
        dto.setId(ma.getId());
        dto.setNombre(ma.getNombre());
        dto.setEspecie(ma.getEspecie());
        dto.setEdad(ma.getEdad());
        dto.setDescripcion(ma.getDescripcion());
        dto.setDisponible(ma.getDisponible());
        return dto;
    }
}
