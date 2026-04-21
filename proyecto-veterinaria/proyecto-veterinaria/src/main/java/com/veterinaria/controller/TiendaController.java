package com.veterinaria.controller;

import com.veterinaria.model.Producto;
import com.veterinaria.model.Venta;
import com.veterinaria.service.ProductoService;
import com.veterinaria.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tienda")
public class TiendaController {

    private final ProductoService productoService;
    private final VentaService ventaService;

    public TiendaController(ProductoService productoService, VentaService ventaService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
    }

    // rutas para productos

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(producto.getNombre());
                    existente.setPrecio(producto.getPrecio());
                    existente.setStock(producto.getStock());
                    existente.setCategoria(producto.getCategoria());
                    return ResponseEntity.ok(productoService.guardar(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        if (productoService.buscarPorId(id).isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.ok("eliminado");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/productos/bajo-stock")
    public ResponseEntity<List<Producto>> productosBajoStock(@RequestParam(required = false, defaultValue = "10") Integer limite) {
        return ResponseEntity.ok(productoService.productosBajoStock(limite));
    }

    // rutas para ventas

    @PostMapping("/ventas")
    public ResponseEntity<Venta> crearVenta(@RequestBody Venta venta) {
        return ResponseEntity.ok(ventaService.registrarVenta(venta));
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
