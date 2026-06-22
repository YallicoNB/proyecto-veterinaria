package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VacunaRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    private String nombreVacuna;

    private LocalDate fechaAplicacion;
    private LocalDate fechaProxima;
    private String lote;
}
