package com.veterinaria.controller;

import com.veterinaria.dto.request.ProductoRequest;
import com.veterinaria.dto.response.ProductoResponse;
import com.veterinaria.dto.response.VentaResponse;
import com.veterinaria.model.Producto;
import com.veterinaria.model.Venta;
import com.veterinaria.service.ProductoService;
import com.veterinaria.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tienda")
public class TiendaController {

    private final ProductoService productoService;
    private final VentaService ventaService;

    public TiendaController(ProductoService productoService, VentaService ventaService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
    }

    // ── Productos ────────────────────────────────────────────────

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        List<ProductoResponse> response = productoService.listarTodos()
                .stream()
                .map(ProductoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> obtenerProducto(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(p -> ResponseEntity.ok(ProductoResponse.fromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(request.getCategoria());
        return ResponseEntity.ok(ProductoResponse.fromEntity(productoService.guardar(producto)));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable Long id,
                                                                @Valid @RequestBody ProductoRequest request) {
        return productoService.buscarPorId(id)
                .map(existente -> {
                    existente.setNombre(request.getNombre());
                    existente.setPrecio(request.getPrecio());
                    existente.setStock(request.getStock());
                    existente.setCategoria(request.getCategoria());
                    return ResponseEntity.ok(ProductoResponse.fromEntity(productoService.guardar(existente)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.buscarPorId(id).isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/productos/bajo-stock")
    public ResponseEntity<List<ProductoResponse>> productosBajoStock(
            @RequestParam(required = false, defaultValue = "10") Integer limite) {
        List<ProductoResponse> response = productoService.productosBajoStock(limite)
                .stream()
                .map(ProductoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // rutas para ventas

    @PostMapping("/ventas")
    public ResponseEntity<VentaResponse> crearVenta(@Valid @RequestBody Venta venta) {
        return ResponseEntity.ok(VentaResponse.fromEntity(ventaService.registrarVenta(venta)));
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<VentaResponse>> listarVentas() {
        List<VentaResponse> response = ventaService.listarTodas()
                .stream()
                .map(VentaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<VentaResponse> obtenerVenta(@PathVariable Long id) {
        return ventaService.buscarPorId(id)
                .map(v -> ResponseEntity.ok(VentaResponse.fromEntity(v)))
                .orElse(ResponseEntity.notFound().build());
    }
}
