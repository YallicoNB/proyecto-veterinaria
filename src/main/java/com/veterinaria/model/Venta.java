package com.veterinaria.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Double total;

    // detalle de la venta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

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

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { 
        this.detalles = detalles;
        if (detalles != null) {
            for (DetalleVenta d : detalles) {
                d.setVenta(this);
            }
        }
    }
}
