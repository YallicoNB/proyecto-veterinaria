package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultaRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

    private Long veterinarioId;

    @NotBlank(message = "Los síntomas son obligatorios")
    private String sintomas;
}
