package com.veterinaria.repository;

import com.veterinaria.model.ServicioLavado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServicioLavadoRepository extends JpaRepository<ServicioLavado, Long> {

    List<ServicioLavado> findByEstado(String estado);

    List<ServicioLavado> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
