package com.veterinaria.repository;

import com.veterinaria.model.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {
    List<SolicitudAdopcion> findByUsuarioId(Long usuarioId);
}