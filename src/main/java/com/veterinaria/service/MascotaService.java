package com.veterinaria.service;

import com.veterinaria.model.Mascota;
import com.veterinaria.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public Optional<Mascota> buscarPorId(Long id) {
        return mascotaRepository.findById(id);
    }
}
