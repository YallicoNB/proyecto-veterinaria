package com.veterinaria.service;

import com.veterinaria.model.Vacuna;
import com.veterinaria.repository.VacunaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VacunaService {

    private final VacunaRepository vacunaRepository;

    public VacunaService(VacunaRepository vacunaRepository) {
        this.vacunaRepository = vacunaRepository;
    }

    public List<Vacuna> buscarPorMascota(Long idMascota) {
        return vacunaRepository.findByIdMascota(idMascota);
    }

    public List<Vacuna> proximasVacunas() {
        return vacunaRepository.findByFechaProximaBefore(LocalDate.now().plusDays(30));
    }

    public Optional<Vacuna> buscarPorId(Long id) {
        return vacunaRepository.findById(id);
    }

    public Vacuna registrar(Vacuna vacuna) {
        return vacunaRepository.save(vacuna);
    }

    public void eliminar(Long id) {
        vacunaRepository.deleteById(id);
    }
}