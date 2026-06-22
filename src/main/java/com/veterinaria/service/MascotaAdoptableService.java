package com.veterinaria.service;

import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.repository.MascotaAdoptableRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MascotaAdoptableService {

    private final MascotaAdoptableRepository mascotaAdoptableRepository;

    public MascotaAdoptableService(MascotaAdoptableRepository mascotaAdoptableRepository) {
        this.mascotaAdoptableRepository = mascotaAdoptableRepository;
    }

    public Optional<MascotaAdoptable> buscarPorId(Long id) {
        return mascotaAdoptableRepository.findById(id);
    }
}
