package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.SeguimientoSolicitud;
import com.sysrefugios.backend.dto.SeguimientoRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Adopcion;
import com.sysrefugios.backend.model.Seguimiento;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.AdopcionRepository;
import com.sysrefugios.backend.repository.SeguimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguimientoService {

    private final SeguimientoRepository seguimientoRepository;
    private final AdopcionRepository adopcionRepository;

    public SeguimientoService(SeguimientoRepository seguimientoRepository, AdopcionRepository adopcionRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.adopcionRepository = adopcionRepository;
    }

    public List<SeguimientoRespuesta> listar(Long adopcionId, Usuario actor) {
        List<Seguimiento> registros = adopcionId != null
                ? seguimientoRepository.findByAdopcionIdAndActivoTrue(adopcionId)
                : seguimientoRepository.findByActivoTrue();
        return registros.stream().map(seguimiento -> aRespuesta(seguimiento, actor)).toList();
    }

    public SeguimientoRespuesta buscarPorId(Long id, Usuario actor) {
        return aRespuesta(buscarEntidad(id), actor);
    }

    public SeguimientoRespuesta crear(SeguimientoSolicitud solicitud, Usuario actor) {
        Adopcion adopcion = buscarAdopcion(solicitud.getAdopcionId());
        exigirAdopcionPropia(adopcion, actor);

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setAdopcion(adopcion);
        seguimiento.setFechaSeguimiento(solicitud.getFechaSeguimiento());
        seguimiento.setEstadoAnimal(solicitud.getEstadoAnimal());
        seguimiento.setObservaciones(solicitud.getObservaciones());
        seguimiento.setReferenciaMultimedia(solicitud.getReferenciaMultimedia());
        seguimiento.setInformacionContacto(solicitud.getInformacionContacto());

        return aRespuesta(seguimientoRepository.save(seguimiento), actor);
    }

    public SeguimientoRespuesta actualizar(Long id, SeguimientoSolicitud solicitud, Usuario actor) {
        Seguimiento seguimiento = buscarEntidad(id);
        exigirAdopcionPropia(seguimiento.getAdopcion(), actor);
        seguimiento.setFechaSeguimiento(solicitud.getFechaSeguimiento());
        seguimiento.setEstadoAnimal(solicitud.getEstadoAnimal());
        seguimiento.setObservaciones(solicitud.getObservaciones());
        seguimiento.setReferenciaMultimedia(solicitud.getReferenciaMultimedia());
        seguimiento.setInformacionContacto(solicitud.getInformacionContacto());
        return aRespuesta(seguimientoRepository.save(seguimiento), actor);
    }

    public void eliminar(Long id, Usuario actor) {
        Seguimiento seguimiento = buscarEntidad(id);
        exigirAdopcionPropia(seguimiento.getAdopcion(), actor);
        seguimiento.setActivo(false);
        seguimientoRepository.save(seguimiento);
    }

    private void exigirAdopcionPropia(Adopcion adopcion, Usuario actor) {
        ControlAcceso.exigirOrganizacionPropia(adopcion.getOrganizacion(), actor,
                "La adopción de " + adopcion.getAnimal().getNombre());
    }

    private Seguimiento buscarEntidad(Long id) {
        return seguimientoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Seguimiento no encontrado: " + id));
    }

    private Adopcion buscarAdopcion(Long id) {
        return adopcionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adopción no encontrada: " + id));
    }

    private SeguimientoRespuesta aRespuesta(Seguimiento seguimiento, Usuario actor) {
        Adopcion adopcion = seguimiento.getAdopcion();
        boolean mismaOrganizacion = ControlAcceso.esDeLaOrganizacion(adopcion.getOrganizacion(), actor);
        return new SeguimientoRespuesta(
                seguimiento.getId(),
                adopcion.getId(),
                adopcion.getAnimal().getNombre(),
                mismaOrganizacion ? adopcion.getAdoptante().nombreCompleto() : null,
                seguimiento.getFechaSeguimiento(),
                seguimiento.getEstadoAnimal(),
                seguimiento.getObservaciones(),
                seguimiento.getReferenciaMultimedia(),
                mismaOrganizacion ? seguimiento.getInformacionContacto() : null
        );
    }
}
