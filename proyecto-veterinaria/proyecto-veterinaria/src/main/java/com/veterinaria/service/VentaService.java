package com.veterinaria.service;

import com.veterinaria.model.DetalleVenta;
import com.veterinaria.model.Producto;
import com.veterinaria.model.Venta;
import com.veterinaria.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoService productoService;

    public VentaService(VentaRepository ventaRepository, ProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.productoService = productoService;
    }

    // listar todas
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    // buscar por id
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // registramos venta (uso transactional para revertir si falla)
    @Transactional
    public Venta registrarVenta(Venta venta) {
        // linkeamos venta bidireccional
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
                
                // traer precio del bd si no se envio
                if (detalle.getPrecioUnitario() == null) {
                    Optional<Producto> p = productoService.buscarPorId(detalle.getIdProducto());
                    p.ifPresent(producto -> detalle.setPrecioUnitario(producto.getPrecio()));
                }
            }
        }
        
        Venta guardada = ventaRepository.save(venta);
        
        // restamos stock en productos
        if (guardada.getDetalles() != null) {
            for (DetalleVenta detalle : guardada.getDetalles()) {
                productoService.actualizarStock(detalle.getIdProducto(), detalle.getCantidad());
            }
        }
        
        return guardada;
    }
}
