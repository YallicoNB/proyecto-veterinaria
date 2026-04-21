package com.veterinaria.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SolicitudAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreSolicitante;
    private String telefono;
    private String motivo;
    private String estado = "PENDIENTE"; 

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private MascotaAdoptable mascota;
}
