package com.veterinaria.model;

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

    public MascotaAdoptable() {}

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public Integer getEdad() { return edad; }
    public String getDescripcion() { return descripcion; }
    public Boolean getDisponible() { return disponible; }
}
