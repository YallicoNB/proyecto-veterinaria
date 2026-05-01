package com.veterinaria.service;

import com.veterinaria.model.HistoriaClinica;
import com.veterinaria.repository.HistoriaClinicaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository) {
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    public List<HistoriaClinica> listarTodas() {
        return historiaClinicaRepository.findAll();
    }

    public List<HistoriaClinica> buscarPorMascota(Long idMascota) {
        return historiaClinicaRepository.findByIdMascota(idMascota);
    }

    public Optional<HistoriaClinica> buscarPorId(Long id) {
        return historiaClinicaRepository.findById(id);
    }

    public HistoriaClinica guardar(HistoriaClinica historiaClinica) {
        return historiaClinicaRepository.save(historiaClinica);
    }

    public void eliminar(Long id) {
        historiaClinicaRepository.deleteById(id);
    }
}