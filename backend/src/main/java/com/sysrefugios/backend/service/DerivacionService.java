package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.DerivacionSolicitud;
import com.sysrefugios.backend.dto.DerivacionRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.Derivacion;
import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.DerivacionRepository;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DerivacionService {

    private final DerivacionRepository derivacionRepository;
    private final OrganizacionRepository organizacionRepository;
    private final AnimalService animalService;
    private final RescateService rescateService;
    private final TransitoService transitoService;

    public DerivacionService(DerivacionRepository derivacionRepository, OrganizacionRepository organizacionRepository,
                              AnimalService animalService, RescateService rescateService,
                              TransitoService transitoService) {
        this.derivacionRepository = derivacionRepository;
        this.organizacionRepository = organizacionRepository;
        this.animalService = animalService;
        this.rescateService = rescateService;
        this.transitoService = transitoService;
    }

    public List<DerivacionRespuesta> listar(Long animalId) {
        List<Derivacion> derivaciones = animalId != null
                ? derivacionRepository.findByAnimalIdAndActivoTrue(animalId)
                : derivacionRepository.findByActivoTrue();
        return derivaciones.stream().map(this::aRespuesta).toList();
    }

    public DerivacionRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    @Transactional
    public DerivacionRespuesta crear(DerivacionSolicitud solicitud, Usuario actor) {
        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);
        Organizacion origen = buscarOrganizacion(solicitud.getOrganizacionOrigenId());
        Organizacion destino = buscarOrganizacion(solicitud.getOrganizacionDestinoId());

        if (origen.getId().equals(destino.getId())) {
            throw new IllegalArgumentException("La organización de origen y destino no pueden ser la misma");
        }
        if (!animal.getOrganizacion().getId().equals(origen.getId())) {
            throw new IllegalArgumentException(
                    "La organización de origen debe ser la organización que actualmente tiene al animal");
        }

        transitoService.cerrarEnCurso(animal, solicitud.getFechaDerivacion(), "derivación");

        Derivacion derivacion = new Derivacion();
        derivacion.setAnimal(animal);
        derivacion.setOrganizacionOrigen(origen);
        derivacion.setOrganizacionDestino(destino);
        derivacion.setFechaDerivacion(solicitud.getFechaDerivacion());
        derivacion.setMotivo(solicitud.getMotivo());
        derivacion.setObservaciones(solicitud.getObservaciones());
        derivacion = derivacionRepository.save(derivacion);

        animal.setOrganizacion(destino);
        animal.setEstado(EstadoAnimal.DERIVADO);
        animalService.guardar(animal);
        rescateService.marcarDerivado(animal);

        return aRespuesta(derivacion);
    }

    public DerivacionRespuesta actualizar(Long id, DerivacionSolicitud solicitud, Usuario actor) {
        Derivacion derivacion = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(derivacion.getOrganizacionOrigen(), actor, "La derivación");

        derivacion.setFechaDerivacion(solicitud.getFechaDerivacion());
        derivacion.setMotivo(solicitud.getMotivo());
        derivacion.setObservaciones(solicitud.getObservaciones());

        return aRespuesta(derivacionRepository.save(derivacion));
    }

    public void eliminar(Long id, Usuario actor) {
        Derivacion derivacion = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(derivacion.getOrganizacionOrigen(), actor, "La derivación");
        derivacion.setActivo(false);
        derivacionRepository.save(derivacion);
    }

    private Derivacion buscarEntidad(Long id) {
        return derivacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Derivación no encontrada: " + id));
    }

    private Organizacion buscarOrganizacion(Long organizacionId) {
        return organizacionRepository.findByIdAndActivoTrue(organizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + organizacionId));
    }

    private DerivacionRespuesta aRespuesta(Derivacion derivacion) {
        return new DerivacionRespuesta(
                derivacion.getId(),
                derivacion.getAnimal().getId(),
                derivacion.getAnimal().getNombre(),
                derivacion.getOrganizacionOrigen().getId(),
                derivacion.getOrganizacionOrigen().getNombre(),
                derivacion.getOrganizacionDestino().getId(),
                derivacion.getOrganizacionDestino().getNombre(),
                derivacion.getFechaDerivacion(),
                derivacion.getMotivo(),
                derivacion.getObservaciones()
        );
    }
}
