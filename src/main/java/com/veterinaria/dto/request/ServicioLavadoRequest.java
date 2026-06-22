package com.veterinaria.dto.request;

import com.veterinaria.model.TipoServicio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServicioLavadoRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

    @NotNull(message = "El tipo de servicio es obligatorio")
    private TipoServicio tipoServicio;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    private String observaciones;
}
