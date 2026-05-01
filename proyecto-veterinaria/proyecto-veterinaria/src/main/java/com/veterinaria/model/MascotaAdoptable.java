package com.veterinaria.model; // Asegúrate que el package sea este

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MascotaAdoptable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private Integer edad;
    private String descripcion;
    private Boolean disponible = true;
}
