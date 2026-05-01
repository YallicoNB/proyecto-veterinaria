package com.veterinaria.service;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import com.veterinaria.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> buscarPorMascota(Long idMascota) {
        return consultaRepository.findByIdMascota(idMascota);
    }

    public List<Consulta> buscarPorEstado(EstadoConsulta estado) {
        return consultaRepository.findByEstado(estado);
    }

    public Optional<Consulta> buscarPorId(Long id) {
        return consultaRepository.findById(id);
    }

    public Consulta agendar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Optional<Consulta> atender(Long id, Consulta datos) {
        return consultaRepository.findById(id).map(consulta -> {
            consulta.setDiagnostico(datos.getDiagnostico());
            consulta.setReceta(datos.getReceta());
            consulta.setEstado(EstadoConsulta.REALIZADA);
            return consultaRepository.save(consulta);
        });
    }

    public Optional<Consulta> cancelar(Long id) {
        return consultaRepository.findById(id).map(consulta -> {
            consulta.setEstado(EstadoConsulta.CANCELADA);
            return consultaRepository.save(consulta);
        });
    }
}