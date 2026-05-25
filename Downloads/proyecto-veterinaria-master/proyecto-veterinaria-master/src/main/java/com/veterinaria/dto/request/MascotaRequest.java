package com.veterinaria.dto.request;

import com.veterinaria.model.Sexo;
import com.veterinaria.model.TipoMascota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MascotaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de mascota es obligatorio")
    private TipoMascota tipo;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    private Integer edad;
    private Sexo sexo;
    private String color;
    private Double peso;
    private String observaciones;
    private String fotoUrl;
}
