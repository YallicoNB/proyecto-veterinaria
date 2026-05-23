package com.veterinaria.repository;

import com.veterinaria.model.MascotaAdoptable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MascotaAdoptableRepository extends JpaRepository<MascotaAdoptable, Long> {
    // Esto servirá para filtrar solo las que aún no han sido adoptadas
    List<MascotaAdoptable> findByDisponibleTrue();
}
