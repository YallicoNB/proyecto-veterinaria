package com.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "detalles_venta")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a cero")
    @Column(nullable = false)
    private Double precioUnitario;

    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    @JsonIgnore
    private Venta venta;

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            subtotal = precioUnitario * cantidad;
        }
    }
}
