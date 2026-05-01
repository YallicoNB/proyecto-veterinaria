package com.veterinaria.repository;

import com.veterinaria.model.Mascota;
import com.veterinaria.model.EstadoMascota;
import com.veterinaria.model.TipoMascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByEstado(EstadoMascota estado);
    List<Mascota> findByTipo(TipoMascota tipo);
}