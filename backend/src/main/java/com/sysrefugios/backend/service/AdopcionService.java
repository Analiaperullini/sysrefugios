package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.AdopcionSolicitud;
import com.sysrefugios.backend.dto.AdopcionRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Adopcion;
import com.sysrefugios.backend.model.Adoptante;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.AdopcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdopcionService {

    public static final int MESES_PLAZO_CASTRACION = 6;

    private final AdopcionRepository adopcionRepository;
    private final AdoptanteService adoptanteService;
    private final AnimalService animalService;
    private final AlertaCastracionService alertaCastracionService;
    private final TransitoService transitoService;

    public AdopcionService(AdopcionRepository adopcionRepository, AdoptanteService adoptanteService,
                            AnimalService animalService, AlertaCastracionService alertaCastracionService,
                            TransitoService transitoService) {
        this.adopcionRepository = adopcionRepository;
        this.adoptanteService = adoptanteService;
        this.animalService = animalService;
        this.alertaCastracionService = alertaCastracionService;
        this.transitoService = transitoService;
    }

    public List<AdopcionRespuesta> listar(Long animalId, Usuario actor) {
        List<Adopcion> adopciones = animalId != null
                ? adopcionRepository.findByAnimalIdAndActivoTrue(animalId)
                : adopcionRepository.findByActivoTrue();
        return adopciones.stream().map(adopcion -> aRespuesta(adopcion, actor)).toList();
    }

    public List<AdopcionRespuesta> listarAlertasCastracion(Usuario actor) {
        return adopcionRepository
                .findByOrganizacionIdAndAlertaCastracionVencidaTrueAndActivoTrue(actor.getOrganizacion().getId())
                .stream()
                .map(adopcion -> aRespuesta(adopcion, actor))
                .toList();
    }

    public AdopcionRespuesta buscarPorId(Long id, Usuario actor) {
        return aRespuesta(buscarEntidad(id), actor);
    }

    @Transactional
    public AdopcionRespuesta crear(AdopcionSolicitud solicitud, Usuario actor) {
        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);
        Adoptante adoptante = adoptanteService.buscarEntidad(solicitud.getAdoptanteId(), actor);

        if (animal.getEstado() == EstadoAnimal.ADOPTADO) {
            throw new IllegalArgumentException("El animal ya está adoptado");
        }
        validarRequisitos(solicitud);
        transitoService.cerrarEnCurso(animal, solicitud.getFechaAdopcion(), "adopción");

        Adopcion adopcion = new Adopcion();
        adopcion.setAnimal(animal);
        adopcion.setAdoptante(adoptante);
        adopcion.setOrganizacion(actor.getOrganizacion());
        aplicarDatos(adopcion, solicitud);
        adopcion = adopcionRepository.save(adopcion);
        alertaCastracionService.actualizar(adopcion);

        animal.setEstado(EstadoAnimal.ADOPTADO);
        animalService.guardar(animal);

        return aRespuesta(adopcion, actor);
    }

    public AdopcionRespuesta actualizar(Long id, AdopcionSolicitud solicitud, Usuario actor) {
        Adopcion adopcion = buscarEntidadPropia(id, actor);
        validarRequisitos(solicitud);
        aplicarDatos(adopcion, solicitud);
        adopcion = adopcionRepository.save(adopcion);
        alertaCastracionService.actualizar(adopcion);
        return aRespuesta(adopcion, actor);
    }

    public void eliminar(Long id, Usuario actor) {
        Adopcion adopcion = buscarEntidadPropia(id, actor);
        adopcion.setActivo(false);
        adopcionRepository.save(adopcion);

        Animal animal = adopcion.getAnimal();
        if (animal.getEstado() == EstadoAnimal.ADOPTADO) {
            animal.setEstado(EstadoAnimal.EN_ADOPCION);
            animalService.guardar(animal);
        }
    }

    private void validarRequisitos(AdopcionSolicitud solicitud) {
        List<String> faltantes = new ArrayList<>();
        if (!solicitud.getCumpleMayorEdad()) {
            faltantes.add("ser mayor de edad");
        }
        if (!solicitud.getCumpleResponsabilidad()) {
            faltantes.add("asumir la responsabilidad del cuidado");
        }
        if (!solicitud.getCumpleEspacioCerrado()) {
            faltantes.add("contar con un espacio cerrado");
        }
        if (!faltantes.isEmpty()) {
            throw new IllegalArgumentException("No se puede registrar la adopción. Requisitos no cumplidos: "
                    + String.join(", ", faltantes));
        }
    }

    private void aplicarDatos(Adopcion adopcion, AdopcionSolicitud solicitud) {
        adopcion.setFechaAdopcion(solicitud.getFechaAdopcion());
        adopcion.setFechaLimiteCastracion(solicitud.getFechaAdopcion().plusMonths(MESES_PLAZO_CASTRACION));
        adopcion.setCumpleMayorEdad(solicitud.getCumpleMayorEdad());
        adopcion.setCumpleResponsabilidad(solicitud.getCumpleResponsabilidad());
        adopcion.setCumpleEspacioCerrado(solicitud.getCumpleEspacioCerrado());
        adopcion.setCompromisoVacunacion(solicitud.getCompromisoVacunacion());
        adopcion.setCompromisoCastracion(solicitud.getCompromisoCastracion());
        adopcion.setCondiciones(solicitud.getCondiciones());
        adopcion.setObservaciones(solicitud.getObservaciones());
    }

    private Adopcion buscarEntidad(Long id) {
        return adopcionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adopción no encontrada: " + id));
    }

    private Adopcion buscarEntidadPropia(Long id, Usuario actor) {
        Adopcion adopcion = buscarEntidad(id);
        ControlAcceso.exigirOrganizacionPropia(adopcion.getOrganizacion(), actor,
                "La adopción de " + adopcion.getAnimal().getNombre());
        return adopcion;
    }

    private AdopcionRespuesta aRespuesta(Adopcion adopcion, Usuario actor) {
        boolean reservado = !ControlAcceso.esDeLaOrganizacion(adopcion.getOrganizacion(), actor);
        return new AdopcionRespuesta(
                adopcion.getId(),
                adopcion.getAnimal().getId(),
                adopcion.getAnimal().getNombre(),
                reservado ? null : adopcion.getAdoptante().getId(),
                reservado ? null : adopcion.getAdoptante().nombreCompleto(),
                reservado,
                adopcion.getOrganizacion().getId(),
                adopcion.getOrganizacion().getNombre(),
                adopcion.getFechaAdopcion(),
                adopcion.isCumpleMayorEdad(),
                adopcion.isCumpleResponsabilidad(),
                adopcion.isCumpleEspacioCerrado(),
                adopcion.isCompromisoVacunacion(),
                adopcion.isCompromisoCastracion(),
                adopcion.getCondiciones(),
                adopcion.getObservaciones(),
                adopcion.getFechaLimiteCastracion(),
                adopcion.isAlertaCastracionVencida()
        );
    }
}
