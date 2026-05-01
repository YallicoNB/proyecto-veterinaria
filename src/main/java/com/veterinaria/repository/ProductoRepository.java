package com.veterinaria.repository;

import com.veterinaria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // filtro para under stock
    List<Producto> findByStockLessThan(Integer stock);
}
