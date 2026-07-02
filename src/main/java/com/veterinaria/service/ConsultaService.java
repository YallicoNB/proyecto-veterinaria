package com.veterinaria.service;

import com.veterinaria.model.Consulta;
import com.veterinaria.model.EstadoConsulta;
import com.veterinaria.model.HistoriaClinica;
import com.veterinaria.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final HistoriaClinicaService historiaClinicaService;

    public ConsultaService(ConsultaRepository consultaRepository,
                           HistoriaClinicaService historiaClinicaService) {
        this.consultaRepository = consultaRepository;
        this.historiaClinicaService = historiaClinicaService;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> buscarPorMascota(Long idMascota) {
        return consultaRepository.findByMascotaId(idMascota);
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

    @Transactional
    public Optional<Consulta> atender(Long id, Consulta datos) {
        return consultaRepository.findById(id).map(consulta -> {
            consulta.setDiagnostico(datos.getDiagnostico());
            consulta.setReceta(datos.getReceta());
            consulta.setEstado(EstadoConsulta.REALIZADA);
            Consulta saved = consultaRepository.save(consulta);

            // Crear automáticamente un registro en la Historia Clínica
            HistoriaClinica hc = new HistoriaClinica();
            hc.setMascota(saved.getMascota());
            hc.setMotivoConsulta(saved.getSintomas());
            hc.setDiagnostico(saved.getDiagnostico());
            hc.setTratamiento(saved.getReceta());
            historiaClinicaService.guardar(hc);

            return saved;
        });
    }

    public Optional<Consulta> cancelar(Long id) {
        return consultaRepository.findById(id).map(consulta -> {
            consulta.setEstado(EstadoConsulta.CANCELADA);
            return consultaRepository.save(consulta);
        });
    }
}