package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.RescateSolicitud;
import com.sysrefugios.backend.dto.RescateRespuesta;
import com.sysrefugios.backend.dto.TransitoSolicitud;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.EstadoRescate;
import com.sysrefugios.backend.model.EtapaVida;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Rescate;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import com.sysrefugios.backend.repository.RescateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RescateService {

    private final RescateRepository rescateRepository;
    private final OrganizacionRepository organizacionRepository;
    private final AnimalService animalService;
    private final TransitoService transitoService;

    public RescateService(RescateRepository rescateRepository, OrganizacionRepository organizacionRepository,
                           AnimalService animalService, TransitoService transitoService) {
        this.rescateRepository = rescateRepository;
        this.organizacionRepository = organizacionRepository;
        this.animalService = animalService;
        this.transitoService = transitoService;
    }

    public List<RescateRespuesta> listar() {
        return rescateRepository.findByActivoTrue().stream().map(this::aRespuesta).toList();
    }

    public RescateRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    @Transactional
    public RescateRespuesta crear(RescateSolicitud solicitud, Usuario actor) {
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());
        if ((solicitud.getAnimalId() == null) == (solicitud.getAnimal() == null)) {
            throw new IllegalArgumentException(
                    "Hay que indicar el animal ya registrado o los datos del animal que ingresa (uno de los dos)");
        }

        Animal animal = null;
        EtapaVida etapaVida;
        ControlAcceso.exigirOrganizacionPropia(organizacion, actor, "El ingreso que se registra");
        if (solicitud.getAnimalId() != null) {
            animal = animalService.buscarEntidad(solicitud.getAnimalId());
            ControlAcceso.exigirAnimalACargo(animal, actor);
            etapaVida = animal.getEtapaVida();
            if (rescateRepository.findFirstByAnimalIdAndActivoTrue(animal.getId()).isPresent()) {
                throw new IllegalArgumentException("El animal ya tiene un rescate registrado");
            }
        } else {
            etapaVida = solicitud.getAnimal().getEtapaVida();
        }

        if (etapaVida == EtapaVida.CACHORRO
                && !Boolean.TRUE.equals(solicitud.getConfirmaAlimentacionAutonoma())) {
            throw new IllegalArgumentException(
                    "Para registrar el ingreso de un cachorro hay que confirmar que ya puede alimentarse solo");
        }

        if (animal == null) {
            animal = animalService.crearIngreso(solicitud.getAnimal(), organizacion);
        }

        Rescate rescate = new Rescate();
        rescate.setAnimal(animal);
        rescate.setOrganizacion(organizacion);
        rescate.setFechaRescate(solicitud.getFechaRescate());
        rescate.setLugar(solicitud.getLugar());
        rescate.setDescripcion(solicitud.getDescripcion());
        rescate.setObservaciones(solicitud.getObservaciones());
        rescate.setEstado(EstadoRescate.EN_CURSO);
        rescate.setUsuario(actor);
        rescate = rescateRepository.save(rescate);

        animal.setEstado(EstadoAnimal.RESCATADO);
        animalService.guardar(animal);

        if (solicitud.getEspacioTransitoId() != null) {
            TransitoSolicitud transito = new TransitoSolicitud();
            transito.setAnimalId(animal.getId());
            transito.setEspacioTransitoId(solicitud.getEspacioTransitoId());
            transito.setFechaInicio(solicitud.getFechaRescate());
            transito.setConfirmaAlimentacionAutonoma(solicitud.getConfirmaAlimentacionAutonoma());
            transitoService.crear(transito, actor);
        }

        return aRespuesta(rescate);
    }

    public RescateRespuesta actualizar(Long id, RescateSolicitud solicitud, Usuario actor) {
        Rescate rescate = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(rescate.getAnimal(), actor);
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());

        rescate.setOrganizacion(organizacion);
        rescate.setFechaRescate(solicitud.getFechaRescate());
        rescate.setLugar(solicitud.getLugar());
        rescate.setDescripcion(solicitud.getDescripcion());
        rescate.setObservaciones(solicitud.getObservaciones());
        if (solicitud.getEstado() != null) {
            rescate.setEstado(solicitud.getEstado());
        }

        return aRespuesta(rescateRepository.save(rescate));
    }

    public void eliminar(Long id, Usuario actor) {
        Rescate rescate = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(rescate.getAnimal(), actor);
        rescate.setActivo(false);
        rescateRepository.save(rescate);
    }

    void marcarDerivado(Animal animal) {
        rescateRepository.findFirstByAnimalIdAndActivoTrue(animal.getId())
                .filter(rescate -> rescate.getEstado() == EstadoRescate.EN_CURSO)
                .ifPresent(rescate -> {
                    rescate.setEstado(EstadoRescate.DERIVADO);
                    rescateRepository.save(rescate);
                });
    }

    private Rescate buscarEntidad(Long id) {
        return rescateRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rescate no encontrado: " + id));
    }

    private Organizacion buscarOrganizacion(Long organizacionId) {
        return organizacionRepository.findByIdAndActivoTrue(organizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + organizacionId));
    }

    private RescateRespuesta aRespuesta(Rescate rescate) {
        return new RescateRespuesta(
                rescate.getId(),
                rescate.getAnimal().getId(),
                rescate.getAnimal().getNombre(),
                rescate.getFechaRescate(),
                rescate.getLugar(),
                rescate.getDescripcion(),
                rescate.getObservaciones(),
                rescate.getOrganizacion().getId(),
                rescate.getOrganizacion().getNombre(),
                rescate.getEstado(),
                rescate.getUsuario().getId(),
                rescate.getUsuario().nombreCompleto(),
                animalService.avisoDerivacion(rescate.getAnimal())
        );
    }
}
