package com.veterinaria.service;

import com.veterinaria.model.DetalleVenta;
import com.veterinaria.model.Producto;
import com.veterinaria.model.Venta;
import com.veterinaria.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void testListarTodas() {
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(new Venta(), new Venta()));

        List<Venta> resultado = ventaService.listarTodas();

        assertEquals(2, resultado.size());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_existe() {
        Venta venta = new Venta();
        venta.setId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Optional<Venta> resultado = ventaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venta> resultado = ventaService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testRegistrarVenta_conDetallesYStock() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setPrecio(100.0);
        producto.setStock(10);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(2);

        Venta venta = new Venta();
        venta.addDetalle(detalle);

        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.registrarVenta(venta);

        assertNotNull(resultado);
        verify(ventaRepository, times(1)).save(venta);
        verify(productoService, times(1)).actualizarStock(1L, 2);
    }

    @Test
    void testRegistrarVenta_precioUnitarioDesdeProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setPrecio(250.0);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(null); // no se envía precio

        Venta venta = new Venta();
        venta.addDetalle(detalle);

        when(productoService.buscarPorId(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.registrarVenta(venta);

        assertNotNull(resultado);
        assertEquals(250.0, detalle.getPrecioUnitario());
    }

    @Test
    void testRegistrarVenta_sinDetalles() {
        Venta venta = new Venta();
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.registrarVenta(venta);

        assertNotNull(resultado);
        verify(productoService, never()).actualizarStock(anyLong(), anyInt());
    }
}
