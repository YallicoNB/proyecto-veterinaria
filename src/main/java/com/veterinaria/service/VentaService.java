package com.veterinaria.service;

import com.veterinaria.exception.BusinessException;
import com.veterinaria.exception.ResourceNotFoundException;
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
        if (venta.getDetalles() != null) {
            // validar stock suficiente antes de procesar
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
                Producto producto = productoService.buscarPorId(detalle.getProducto().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Producto", detalle.getProducto().getId()));
                if (producto.getStock() == null || producto.getStock() < detalle.getCantidad()) {
                    throw new BusinessException(
                            "Stock insuficiente para el producto '" + producto.getNombre() +
                            "': disponible " + producto.getStock() +
                            ", solicitado " + detalle.getCantidad());
                }
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(producto.getPrecio());
                }
            }
        }

        Venta guardada = ventaRepository.save(venta);

        // restamos stock en productos
        if (guardada.getDetalles() != null) {
            for (DetalleVenta detalle : guardada.getDetalles()) {
                productoService.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad());
            }
        }

        return guardada;
    }
}
