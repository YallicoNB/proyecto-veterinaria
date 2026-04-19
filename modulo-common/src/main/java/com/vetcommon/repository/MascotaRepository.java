package com.vetcommon.repository;

import com.vetcommon.model.Mascota;
import com.vetcommon.model.EstadoMascota;
import com.vetcommon.model.TipoMascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByEstado(EstadoMascota estado);
    List<Mascota> findByTipo(TipoMascota tipo);
}