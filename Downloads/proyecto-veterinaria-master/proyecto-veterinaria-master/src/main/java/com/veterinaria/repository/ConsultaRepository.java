package com.veterinaria.repository;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMascotaId(Long mascotaId);

    List<Consulta> findByEstado(EstadoConsulta estado);

    List<Consulta> findByVeterinarioId(Long veterinarioId);

    @Query("SELECT c FROM Consulta c WHERE c.sintomas = :sintoma")
    List<Consulta> findBySintomaExacto(@Param("sintoma") String sintoma);
}