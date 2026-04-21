package com.veterinaria.service;

import com.veterinaria.model.ServicioLavado;
import com.veterinaria.repository.ServicioLavadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LavanderiaService {

    @Autowired
    private ServicioLavadoRepository servicioLavadoRepository;

    public ServicioLavado crearServicio(ServicioLavado servicio) {
        servicio.setEstado("PENDIENTE");
        return servicioLavadoRepository.save(servicio);
    }

    public ServicioLavado cambiarEstado(Long id, String nuevoEstado) {
        ServicioLavado servicio = servicioLavadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
        validarEstado(nuevoEstado);
        servicio.setEstado(nuevoEstado);
        return servicioLavadoRepository.save(servicio);
    }

    public List<ServicioLavado> listarPendientes() {
        return servicioLavadoRepository.findByEstado("PENDIENTE");
    }

    public List<ServicioLavado> listarTodos() {
        return servicioLavadoRepository.findAll();
    }

    public ServicioLavado obtenerPorId(Long id) {
        return servicioLavadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
    }

    private void validarEstado(String estado) {
        List<String> estadosValidos = List.of("PENDIENTE", "EN_PROCESO", "TERMINADO", "ENTREGADO");
        if (!estadosValidos.contains(estado)) {
            throw new IllegalArgumentException("Estado inválido: " + estado +
                    ". Estados permitidos: " + estadosValidos);
        }
    }
}