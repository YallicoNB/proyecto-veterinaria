package com.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vacunas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    @NotNull(message = "La mascota es obligatoria")
    private Mascota mascota;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    @Column(nullable = false)
    private String nombreVacuna;

    private LocalDate fechaAplicacion;
    private LocalDate fechaProxima;
    private String lote;
}