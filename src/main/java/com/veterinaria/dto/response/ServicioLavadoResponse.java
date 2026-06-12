package com.veterinaria.dto.response;

import com.veterinaria.model.ServicioLavado;
import com.veterinaria.model.TipoServicio;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServicioLavadoResponse {

    private Long id;
    private Long mascotaId;
    private String nombreMascota;
    private TipoServicio tipoServicio;
    private Double precio;
    private LocalDateTime fechaHora;
    private String estado;
    private String observaciones;

    public static ServicioLavadoResponse fromEntity(ServicioLavado sl) {
        if (sl == null) return null;
        ServicioLavadoResponse dto = new ServicioLavadoResponse();
        dto.setId(sl.getId());
        if (sl.getMascota() != null) {
            dto.setMascotaId(sl.getMascota().getId());
            dto.setNombreMascota(sl.getMascota().getNombre());
        }
        dto.setTipoServicio(sl.getTipoServicio());
        dto.setPrecio(sl.getPrecio());
        dto.setFechaHora(sl.getFechaHora());
        dto.setEstado(sl.getEstado());
        dto.setObservaciones(sl.getObservaciones());
        return dto;
    }
}
