package com.veterinaria.dto.response;

import com.veterinaria.model.Venta;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VentaResponse {

    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleVentaResponse> detalles = new ArrayList<>();

    public static VentaResponse fromEntity(Venta venta) {
        if (venta == null) return null;
        VentaResponse dto = new VentaResponse();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        if (venta.getDetalles() != null) {
            dto.setDetalles(venta.getDetalles().stream()
                    .map(DetalleVentaResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
