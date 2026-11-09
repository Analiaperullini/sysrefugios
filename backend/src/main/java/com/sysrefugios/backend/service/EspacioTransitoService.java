package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.EspacioTransitoSolicitud;
import com.sysrefugios.backend.dto.EspacioTransitoRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.EspacioTransito;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.EspacioTransitoRepository;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import com.sysrefugios.backend.repository.TransitoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspacioTransitoService {

    private final EspacioTransitoRepository espacioTransitoRepository;
    private final OrganizacionRepository organizacionRepository;
    private final TransitoRepository transitoRepository;

    public EspacioTransitoService(EspacioTransitoRepository espacioTransitoRepository,
                                  OrganizacionRepository organizacionRepository,
                                  TransitoRepository transitoRepository) {
        this.espacioTransitoRepository = espacioTransitoRepository;
        this.organizacionRepository = organizacionRepository;
        this.transitoRepository = transitoRepository;
    }

    public List<EspacioTransitoRespuesta> listar(Long organizacionId) {
        List<EspacioTransito> espacios = organizacionId != null
                ? espacioTransitoRepository.findByOrganizacionIdAndActivoTrue(organizacionId)
                : espacioTransitoRepository.findByActivoTrue();
        return espacios.stream().map(this::aRespuesta).toList();
    }

    public EspacioTransitoRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public EspacioTransitoRespuesta crear(EspacioTransitoSolicitud solicitud, Usuario actor) {
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());
        ControlAcceso.exigirOrganizacionPropia(organizacion, actor, "El espacio de tránsito");
        EspacioTransito espacio = new EspacioTransito();
        espacio.setOrganizacion(organizacion);
        aplicarDatos(espacio, solicitud);
        return aRespuesta(espacioTransitoRepository.save(espacio));
    }

    public EspacioTransitoRespuesta actualizar(Long id, EspacioTransitoSolicitud solicitud, Usuario actor) {
        EspacioTransito espacio = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(espacio.getOrganizacion(), actor, "El espacio " + espacio.getNombre());
        long ocupacion = ocupacion(espacio);
        if (solicitud.getCapacidad() < ocupacion) {
            throw new IllegalArgumentException("La capacidad no puede ser menor que la ocupación actual ("
                    + ocupacion + " tránsitos activos)");
        }
        aplicarDatos(espacio, solicitud);
        return aRespuesta(espacioTransitoRepository.save(espacio));
    }

    public void eliminar(Long id, Usuario actor) {
        EspacioTransito espacio = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(espacio.getOrganizacion(), actor, "El espacio " + espacio.getNombre());
        if (ocupacion(espacio) > 0) {
            throw new IllegalArgumentException("No se puede dar de baja un espacio con tránsitos activos");
        }
        espacio.setActivo(false);
        espacioTransitoRepository.save(espacio);
    }

    EspacioTransito buscarEntidad(Long id) {
        return espacioTransitoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Espacio de tránsito no encontrado: " + id));
    }

    long ocupacion(EspacioTransito espacio) {
        return transitoRepository.countByEspacioTransitoIdAndFechaFinIsNullAndActivoTrue(espacio.getId());
    }

    private Organizacion buscarOrganizacion(Long organizacionId) {
        return organizacionRepository.findByIdAndActivoTrue(organizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + organizacionId));
    }

    private void aplicarDatos(EspacioTransito espacio, EspacioTransitoSolicitud solicitud) {
        espacio.setNombre(solicitud.getNombre());
        espacio.setDescripcion(solicitud.getDescripcion());
        espacio.setCapacidad(solicitud.getCapacidad());
        espacio.setEstado(solicitud.getEstado());
        espacio.setObservaciones(solicitud.getObservaciones());
    }

    private EspacioTransitoRespuesta aRespuesta(EspacioTransito espacio) {
        return new EspacioTransitoRespuesta(
                espacio.getId(),
                espacio.getOrganizacion().getId(),
                espacio.getOrganizacion().getNombre(),
                espacio.getNombre(),
                espacio.getDescripcion(),
                espacio.getCapacidad(),
                ocupacion(espacio),
                espacio.getEstado(),
                espacio.getObservaciones()
        );
    }
}
