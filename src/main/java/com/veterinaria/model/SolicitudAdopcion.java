package com.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SolicitudAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String nombreSolicitante;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String estado = "PENDIENTE"; 

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    @NotNull(message = "La mascota a adoptar es obligatoria")
    private MascotaAdoptable mascota;
}
