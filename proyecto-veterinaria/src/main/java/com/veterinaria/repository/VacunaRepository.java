package com.veterinaria.repository;

import com.veterinaria.model.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
    List<Vacuna> findByIdMascota(Long idMascota);

    List<Vacuna> findByFechaProximaBefore(LocalDate fecha);
}