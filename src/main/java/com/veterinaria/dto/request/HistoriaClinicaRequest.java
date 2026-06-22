package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistoriaClinicaRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

    @NotBlank(message = "El motivo de la consulta es obligatorio")
    private String motivoConsulta;

    private String diagnostico;
    private String tratamiento;
}
