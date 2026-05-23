package com.veterinaria.dto.response;

import com.veterinaria.model.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MascotaResponse {

    private Long id;
    private String nombre;
    private TipoMascota tipo;
    private String raza;
    private Integer edad;
    private Sexo sexo;
    private String color;
    private Double peso;
    private String observaciones;
    private String fotoUrl;
    private EstadoMascota estado;
    private LocalDateTime fechaRegistro;

    public static MascotaResponse fromEntity(Mascota mascota) {
        MascotaResponse dto = new MascotaResponse();
        dto.setId(mascota.getId());
        dto.setNombre(mascota.getNombre());
        dto.setTipo(mascota.getTipo());
        dto.setRaza(mascota.getRaza());
        dto.setEdad(mascota.getEdad());
        dto.setSexo(mascota.getSexo());
        dto.setColor(mascota.getColor());
        dto.setPeso(mascota.getPeso());
        dto.setObservaciones(mascota.getObservaciones());
        dto.setFotoUrl(mascota.getFotoUrl());
        dto.setEstado(mascota.getEstado());
        dto.setFechaRegistro(mascota.getFechaRegistro());
        return dto;
    }
}
