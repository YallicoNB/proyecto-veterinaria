package com.veterinaria.dto.response;

import com.veterinaria.model.DetalleVenta;
import lombok.Data;

@Data
public class DetalleVentaResponse {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    public static DetalleVentaResponse fromEntity(DetalleVenta dv) {
        if (dv == null) return null;
        DetalleVentaResponse dto = new DetalleVentaResponse();
        dto.setId(dv.getId());
        if (dv.getProducto() != null) {
            dto.setProductoId(dv.getProducto().getId());
            dto.setNombreProducto(dv.getProducto().getNombre());
        }
        dto.setCantidad(dv.getCantidad());
        dto.setPrecioUnitario(dv.getPrecioUnitario());
        dto.setSubtotal(dv.getSubtotal());
        return dto;
    }
}
