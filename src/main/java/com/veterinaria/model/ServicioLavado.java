package com.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "servicios_lavado")
public class ServicioLavado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    @NotNull(message = "La mascota es obligatoria")
    private Mascota mascota;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    @NotNull(message = "El tipo de servicio es obligatorio")
    private TipoServicio tipoServicio;

    @Column(nullable = false)
    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    @Column(name = "fecha_hora", nullable = false)
    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    @NotNull(message = "El estado es obligatorio")
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}