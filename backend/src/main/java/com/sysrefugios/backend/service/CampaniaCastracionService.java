package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.AsignacionCastracionSolicitud;
import com.sysrefugios.backend.dto.AsignacionCastracionRespuesta;
import com.sysrefugios.backend.dto.CambioEstadoAsignacionSolicitud;
import com.sysrefugios.backend.dto.CampaniaCastracionSolicitud;
import com.sysrefugios.backend.dto.CampaniaCastracionRespuesta;
import com.sysrefugios.backend.dto.CastracionSolicitud;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.AsignacionCastracion;
import com.sysrefugios.backend.model.CampaniaCastracion;
import com.sysrefugios.backend.model.EstadoAsignacion;
import com.sysrefugios.backend.model.EstadoCampania;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.AsignacionCastracionRepository;
import com.sysrefugios.backend.repository.CampaniaCastracionRepository;
import com.sysrefugios.backend.repository.CastracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CampaniaCastracionService {

    private final CampaniaCastracionRepository campaniaRepository;
    private final AsignacionCastracionRepository asignacionRepository;
    private final CastracionRepository castracionRepository;
    private final AnimalService animalService;
    private final CastracionService castracionService;

    public CampaniaCastracionService(CampaniaCastracionRepository campaniaRepository,
                                     AsignacionCastracionRepository asignacionRepository,
                                     CastracionRepository castracionRepository, AnimalService animalService,
                                     CastracionService castracionService) {
        this.campaniaRepository = campaniaRepository;
        this.asignacionRepository = asignacionRepository;
        this.castracionRepository = castracionRepository;
        this.animalService = animalService;
        this.castracionService = castracionService;
    }

    public List<CampaniaCastracionRespuesta> listar() {
        return campaniaRepository.findByActivoTrueOrderByFechaDesc().stream().map(this::aRespuesta).toList();
    }

    public CampaniaCastracionRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public CampaniaCastracionRespuesta crear(CampaniaCastracionSolicitud solicitud, Usuario actor) {
        CampaniaCastracion campania = new CampaniaCastracion();
        campania.setOrganizacion(actor.getOrganizacion());
        aplicarDatos(campania, solicitud);
        if (solicitud.getEstado() == null) {
            campania.setEstado(EstadoCampania.PROGRAMADA);
        }
        return aRespuesta(campaniaRepository.save(campania));
    }

    public CampaniaCastracionRespuesta actualizar(Long id, CampaniaCastracionSolicitud solicitud, Usuario actor) {
        CampaniaCastracion campania = buscarEntidadPropia(id, actor);
        long usados = cuposUsados(campania);
        if (solicitud.getCantidadCupos() < usados) {
            throw new IllegalArgumentException("La cantidad de cupos no puede ser menor que los cupos ya asignados ("
                    + usados + ")");
        }
        aplicarDatos(campania, solicitud);
        return aRespuesta(campaniaRepository.save(campania));
    }

    public void eliminar(Long id, Usuario actor) {
        CampaniaCastracion campania = buscarEntidadPropia(id, actor);
        campania.setActivo(false);
        campaniaRepository.save(campania);
    }

    public List<AsignacionCastracionRespuesta> listarAsignaciones(Long campaniaId) {
        buscarEntidad(campaniaId);
        return asignacionRepository.findByCampaniaIdOrderByFechaAsignacionAscIdAsc(campaniaId).stream()
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional
    public AsignacionCastracionRespuesta asignar(Long campaniaId, AsignacionCastracionSolicitud solicitud, Usuario actor) {
        CampaniaCastracion campania = campaniaRepository.buscarParaAsignar(campaniaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Campaña de castración no encontrada: " + campaniaId));
        ControlAcceso.exigirOrganizacionPropia(campania.getOrganizacion(), actor, "La campaña " + campania.getNombre());
        if (campania.getEstado() == EstadoCampania.FINALIZADA) {
            throw new IllegalArgumentException("La campaña " + campania.getNombre() + " ya finalizó");
        }

        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);
        if (castracionRepository.existsByAnimalIdAndActivoTrue(animal.getId())) {
            throw new IllegalArgumentException(animal.getNombre() + " ya tiene una castración registrada");
        }

        AsignacionCastracion asignacion = asignacionRepository
                .findByCampaniaIdAndAnimalId(campania.getId(), animal.getId())
                .orElse(null);
        if (asignacion != null && asignacion.getEstado() != EstadoAsignacion.CANCELADA) {
            throw new IllegalArgumentException(animal.getNombre() + " ya tiene un cupo en esta campaña");
        }

        long usados = cuposUsados(campania);
        if (usados >= campania.getCantidadCupos()) {
            throw new IllegalArgumentException("La campaña " + campania.getNombre() + " no tiene cupos disponibles ("
                    + usados + " / " + campania.getCantidadCupos() + ")");
        }

        if (asignacion == null) {
            asignacion = new AsignacionCastracion();
            asignacion.setCampania(campania);
            asignacion.setAnimal(animal);
        }
        asignacion.setFechaAsignacion(LocalDate.now());
        asignacion.setEstado(EstadoAsignacion.ASIGNADA);
        asignacion.setObservaciones(solicitud.getObservaciones());
        return aRespuesta(asignacionRepository.save(asignacion));
    }

    @Transactional
    public AsignacionCastracionRespuesta cambiarEstado(Long asignacionId, CambioEstadoAsignacionSolicitud solicitud,
                                                      Usuario actor) {
        AsignacionCastracion asignacion = asignacionRepository.findById(asignacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignación no encontrada: " + asignacionId));
        CampaniaCastracion campania = asignacion.getCampania();
        ControlAcceso.exigirOrganizacionPropia(campania.getOrganizacion(), actor, "La campaña " + campania.getNombre());
        if (asignacion.getEstado() != EstadoAsignacion.ASIGNADA) {
            throw new IllegalArgumentException("Solo se puede cambiar una asignación que está en estado ASIGNADA");
        }
        if (solicitud.getEstado() == EstadoAsignacion.ASIGNADA) {
            throw new IllegalArgumentException("La asignación ya está en estado ASIGNADA");
        }
        if (solicitud.getEstado() == EstadoAsignacion.REALIZADA
                && (solicitud.getVeterinario() == null || solicitud.getVeterinario().isBlank())) {
            throw new IllegalArgumentException("Para marcar la castración como realizada hay que indicar el veterinario");
        }

        asignacion.setEstado(solicitud.getEstado());
        if (solicitud.getObservaciones() != null) {
            asignacion.setObservaciones(solicitud.getObservaciones());
        }
        asignacion = asignacionRepository.save(asignacion);

        if (solicitud.getEstado() == EstadoAsignacion.REALIZADA) {
            CastracionSolicitud castracion = new CastracionSolicitud();
            castracion.setAnimalId(asignacion.getAnimal().getId());
            LocalDate hoy = LocalDate.now();
            castracion.setFecha(campania.getFecha().isAfter(hoy) ? hoy : campania.getFecha());
            castracion.setVeterinario(solicitud.getVeterinario());
            castracion.setObservaciones("Campaña " + campania.getNombre());
            castracionService.crear(castracion, actor);
        }
        return aRespuesta(asignacion);
    }

    private long cuposUsados(CampaniaCastracion campania) {
        return asignacionRepository.countByCampaniaIdAndEstadoNot(campania.getId(), EstadoAsignacion.CANCELADA);
    }

    private CampaniaCastracion buscarEntidad(Long id) {
        return campaniaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Campaña de castración no encontrada: " + id));
    }

    private CampaniaCastracion buscarEntidadPropia(Long id, Usuario actor) {
        CampaniaCastracion campania = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(campania.getOrganizacion(), actor, "La campaña " + campania.getNombre());
        return campania;
    }

    private void aplicarDatos(CampaniaCastracion campania, CampaniaCastracionSolicitud solicitud) {
        campania.setNombre(solicitud.getNombre());
        campania.setFecha(solicitud.getFecha());
        campania.setLugar(solicitud.getLugar());
        campania.setCantidadCupos(solicitud.getCantidadCupos());
        campania.setDescripcion(solicitud.getDescripcion());
        if (solicitud.getEstado() != null) {
            campania.setEstado(solicitud.getEstado());
        }
    }

    private CampaniaCastracionRespuesta aRespuesta(CampaniaCastracion campania) {
        return new CampaniaCastracionRespuesta(
                campania.getId(),
                campania.getOrganizacion().getId(),
                campania.getOrganizacion().getNombre(),
                campania.getNombre(),
                campania.getFecha(),
                campania.getLugar(),
                campania.getCantidadCupos(),
                cuposUsados(campania),
                campania.getDescripcion(),
                campania.getEstado()
        );
    }

    private AsignacionCastracionRespuesta aRespuesta(AsignacionCastracion asignacion) {
        return new AsignacionCastracionRespuesta(
                asignacion.getId(),
                asignacion.getCampania().getId(),
                asignacion.getCampania().getNombre(),
                asignacion.getAnimal().getId(),
                asignacion.getAnimal().getNombre(),
                asignacion.getAnimal().getOrganizacion().getNombre(),
                asignacion.getFechaAsignacion(),
                asignacion.getEstado(),
                asignacion.getObservaciones()
        );
    }
}
