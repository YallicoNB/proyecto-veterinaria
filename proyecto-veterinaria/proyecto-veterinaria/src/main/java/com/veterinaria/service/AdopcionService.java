package com.veterinaria.service;

import com.veterinaria.model.MascotaAdoptable;
import com.veterinaria.model.SolicitudAdopcion;
import com.veterinaria.repository.MascotaAdoptableRepository;
import com.veterinaria.repository.SolicitudAdopcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdopcionService {

    @Autowired
    private MascotaAdoptableRepository mascotaRepo;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepo;

    // Listar solo las mascotas disponibles
    public List<MascotaAdoptable> listarDisponibles() {
        return mascotaRepo.findByDisponibleTrue();
    }

    // Guardar una nueva solicitud
    public SolicitudAdopcion crearSolicitud(SolicitudAdopcion solicitud) {
        solicitud.setEstado("PENDIENTE");
        return solicitudRepo.save(solicitud);
    }

    // Lógica para aprobar/rechazar
    public SolicitudAdopcion cambiarEstado(Long id, String nuevoEstado) {
        SolicitudAdopcion solicitud = solicitudRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
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
