package com.veterinaria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
        calcularTotal();
    }
    
    @PreUpdate
    protected void onUpdate() {
        calcularTotal();
    }

    private void calcularTotal() {
        if (detalles != null) {
            total = detalles.stream().mapToDouble(d -> {
                if (d.getPrecioUnitario() != null && d.getCantidad() != null) {
                    return d.getPrecioUnitario() * d.getCantidad();
                }
                return 0.0;
            }).sum();
        } else {
            total = 0.0;
        }
    }

    public void addDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void removeDetalle(DetalleVenta detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }

    public void setDetalles(List<DetalleVenta> detalles) { 
        this.detalles = detalles;
        if (detalles != null) {
            for (DetalleVenta d : detalles) {
                d.setVenta(this);
            }
        }
    }
}
