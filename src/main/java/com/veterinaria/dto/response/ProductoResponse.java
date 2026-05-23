package com.veterinaria.dto.response;

import com.veterinaria.model.Producto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductoResponse {

    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String categoria;
    private LocalDateTime fechaRegistro;

    public static ProductoResponse fromEntity(Producto producto) {
        ProductoResponse dto = new ProductoResponse();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoria(producto.getCategoria());
        dto.setFechaRegistro(producto.getFechaRegistro());
        return dto;
    }
}
