package com.veterinaria.controller;

import com.veterinaria.model.Producto;
import com.veterinaria.model.Venta;
import com.veterinaria.service.ProductoService;
import com.veterinaria.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TiendaController.class)
@AutoConfigureMockMvc(addFilters = false)
class TiendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private VentaService ventaService;

    @Test
    void listarProductos_debeRetornar200() throws Exception {
        when(productoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/tienda/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void obtenerProducto_cuandoExiste_debeRetornar200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Alimento");
        when(productoService.buscarPorId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/tienda/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Alimento"));
    }

    @Test
    void obtenerProducto_cuandoNoExiste_debeRetornar404() throws Exception {
        when(productoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tienda/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearProducto_debeRetornar200() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Nuevo Producto");
        producto.setPrecio(100.0);
        producto.setStock(10);
        when(productoService.guardar(any(Producto.class))).thenReturn(producto);

        String json = """
                {
                    "nombre": "Nuevo Producto",
                    "precio": 100.0,
                    "stock": 10
                }
                """;

        mockMvc.perform(post("/api/tienda/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo Producto"));
    }

    @Test
    void eliminarProducto_cuandoExiste_debeRetornar204() throws Exception {
        when(productoService.buscarPorId(1L)).thenReturn(Optional.of(new Producto()));

        mockMvc.perform(delete("/api/tienda/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarProducto_cuandoNoExiste_debeRetornar404() throws Exception {
        when(productoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tienda/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void productosBajoStock_debeRetornar200() throws Exception {
        when(productoService.productosBajoStock(10)).thenReturn(List.of(new Producto()));

        mockMvc.perform(get("/api/tienda/productos/bajo-stock"))
                .andExpect(status().isOk());
    }

    @Test
    void crearVenta_debeRetornar200() throws Exception {
        when(ventaService.registrarVenta(any(Venta.class))).thenReturn(new Venta());

        String json = """
                {
                    "detalles": [
                        {"producto": {"id": 1}, "cantidad": 2, "precioUnitario": 100.0}
                    ]
                }
                """;

        mockMvc.perform(post("/api/tienda/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void listarVentas_debeRetornar200() throws Exception {
        when(ventaService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/tienda/ventas"))
                .andExpect(status().isOk());
    }
}
