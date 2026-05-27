package com.veterinaria.service;

import com.veterinaria.model.Producto;
import com.veterinaria.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // listar
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // buscar id
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // guardar
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // borrar
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    // ver productos bajo stock
    public List<Producto> productosBajoStock(Integer limite) {
        return productoRepository.findByStockLessThan(limite);
    }

    // restamos stock luego de comprar
    public void actualizarStock(Long id, Integer cantidadVendida) {
        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isPresent()) {
            Producto p = opt.get();
            int nuevoStock = p.getStock() - cantidadVendida;
            p.setStock(nuevoStock < 0 ? 0 : nuevoStock);
            productoRepository.save(p);
        }
    }
}
