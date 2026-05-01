package com.veterinaria.repository;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByIdMascota(Long idMascota);

    List<Consulta> findByEstado(EstadoConsulta estado);

    List<Consulta> findByIdVeterinario(Long idVeterinario);
}