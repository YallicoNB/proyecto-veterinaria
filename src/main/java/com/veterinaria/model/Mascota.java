package com.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de mascota es obligatorio")
    @Column(nullable = false)
    private TipoMascota tipo;

    @NotBlank(message = "La raza es obligatoria")
    @Column(nullable = false)
    private String raza;

    private Integer edad;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String color;
    private Double peso;
    private String observaciones;
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    private EstadoMascota estado;

    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        if (estado == null) estado = EstadoMascota.ACTIVA;
    }
}