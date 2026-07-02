package com.veterinaria.service;

import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.repository.MascotaAdoptableRepository;
import com.veterinaria.repository.SolicitudAdopcionRepository;
import com.veterinaria.exception.BusinessException;
import com.veterinaria.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdopcionService {

    private final MascotaAdoptableRepository mascotaRepo;
    private final SolicitudAdopcionRepository solicitudRepo;

    public AdopcionService(MascotaAdoptableRepository mascotaRepo, SolicitudAdopcionRepository solicitudRepo) {
        this.mascotaRepo = mascotaRepo;
        this.solicitudRepo = solicitudRepo;
    }

    // Listar solo las mascotas disponibles
    public List<MascotaAdoptable> listarDisponibles() {
        return mascotaRepo.findByDisponibleTrue();
    }

    // Listar todas las solicitudes
    public List<SolicitudAdopcion> listarSolicitudes() {
        return solicitudRepo.findAll();
    }

    // Listar solicitudes de un usuario
    public List<SolicitudAdopcion> listarPorUsuario(Long usuarioId) {
        return solicitudRepo.findByUsuarioId(usuarioId);
    }

    // Guardar una nueva solicitud
    public SolicitudAdopcion crearSolicitud(SolicitudAdopcion solicitud) {
        solicitud.setEstado("PENDIENTE");
        return solicitudRepo.save(solicitud);
    }

    // Lógica para aprobar/rechazar
    public SolicitudAdopcion cambiarEstado(Long id, String nuevoEstado) {
        SolicitudAdopcion solicitud = solicitudRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SolicitudAdopcion", id));

        solicitud.setEstado(nuevoEstado);

        // Si se aprueba, la mascota ya no está disponible para otros
        if ("APROBADA".equalsIgnoreCase(nuevoEstado)) {
            MascotaAdoptable mascota = solicitud.getMascota();
            mascota.setDisponible(false);
            mascotaRepo.save(mascota);
        }

        return solicitudRepo.save(solicitud);
    }
}
