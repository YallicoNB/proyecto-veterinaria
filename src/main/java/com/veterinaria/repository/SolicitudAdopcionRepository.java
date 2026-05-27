package com.veterinaria.repository;

import com.veterinaria.model.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {
}