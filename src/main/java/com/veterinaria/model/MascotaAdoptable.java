package com.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MascotaAdoptable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    @NotNull(message = "La edad es obligatoria")
    private Integer edad;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private Boolean disponible = true;
}
