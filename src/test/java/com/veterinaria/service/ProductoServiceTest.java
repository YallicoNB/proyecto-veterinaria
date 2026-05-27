package com.veterinaria.service;

import com.veterinaria.model.Producto;
import com.veterinaria.repository.ProductoRepository;
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
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testListarTodos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(new Producto(), new Producto(), new Producto()));

        List<Producto> resultado = productoService.listarTodos();

        assertEquals(3, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_existe() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGuardar() {
        Producto producto = new Producto();
        producto.setNombre("Alimento");
        producto.setPrecio(100.0);
        producto.setStock(10);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Alimento", resultado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testEliminar() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminar(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testProductosBajoStock() {
        Producto p1 = new Producto();
        p1.setStock(3);
        Producto p2 = new Producto();
        p2.setStock(7);
        when(productoRepository.findByStockLessThan(10)).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.productosBajoStock(10);

        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findByStockLessThan(10);
    }

    @Test
    void testActualizarStock_disminuyeCorrectamente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(10);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        productoService.actualizarStock(1L, 3);

        assertEquals(7, producto.getStock());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testActualizarStock_noPermiteNegativo() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(5);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.actualizarStock(1L, 10);

        assertEquals(0, producto.getStock());
    }

    @Test
    void testActualizarStock_productoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        productoService.actualizarStock(99L, 5);

        verify(productoRepository, never()).save(any());
    }
}
