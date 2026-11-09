package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.TransitoSolicitud;
import com.sysrefugios.backend.dto.TransitoRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.EspacioTransito;
import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.EstadoEspacioTransito;
import com.sysrefugios.backend.model.EstadoTransito;
import com.sysrefugios.backend.model.EtapaVida;
import com.sysrefugios.backend.model.Transito;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.TransitoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransitoService {

    private final TransitoRepository transitoRepository;
    private final EspacioTransitoService espacioTransitoService;
    private final AnimalService animalService;

    public TransitoService(TransitoRepository transitoRepository, EspacioTransitoService espacioTransitoService,
                            AnimalService animalService) {
        this.transitoRepository = transitoRepository;
        this.espacioTransitoService = espacioTransitoService;
        this.animalService = animalService;
    }

    public List<TransitoRespuesta> listar(Long animalId) {
        List<Transito> transitos = animalId != null
                ? transitoRepository.findByAnimalIdAndActivoTrue(animalId)
                : transitoRepository.findByActivoTrue();
        return transitos.stream().map(this::aRespuesta).toList();
    }

    public TransitoRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public TransitoRespuesta crear(TransitoSolicitud solicitud, Usuario actor) {
        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);
        EspacioTransito espacio = espacioTransitoService.buscarEntidad(solicitud.getEspacioTransitoId());
        validarFechas(solicitud);

        if (!espacio.getOrganizacion().getId().equals(animal.getOrganizacion().getId())) {
            throw new IllegalArgumentException("El espacio pertenece a " + espacio.getOrganizacion().getNombre()
                    + " y el animal está a cargo de " + animal.getOrganizacion().getNombre()
                    + ": primero hay que registrar la derivación");
        }
        if (animal.getEtapaVida() == EtapaVida.CACHORRO
                && !Boolean.TRUE.equals(solicitud.getConfirmaAlimentacionAutonoma())) {
            throw new IllegalArgumentException(
                    "Para registrar el ingreso de un cachorro hay que confirmar que ya puede alimentarse solo");
        }
        if (espacio.getEstado() == EstadoEspacioTransito.INACTIVO) {
            throw new IllegalArgumentException("El espacio " + espacio.getNombre() + " no está recibiendo ingresos");
        }
        if (solicitud.getFechaFin() == null) {
            validarCapacidad(espacio);
        }

        Transito transito = new Transito();
        transito.setAnimal(animal);
        transito.setEspacioTransito(espacio);
        transito.setFechaInicio(solicitud.getFechaInicio());
        transito.setFechaFin(solicitud.getFechaFin());
        transito.setEstado(solicitud.getFechaFin() == null ? EstadoTransito.EN_CURSO : EstadoTransito.FINALIZADO);
        transito.setObservaciones(solicitud.getObservaciones());
        transito = transitoRepository.save(transito);

        if (solicitud.getFechaFin() == null) {
            animal.setEstado(EstadoAnimal.EN_TRANSITO);
            animalService.guardar(animal);
        }

        return aRespuesta(transito);
    }

    public TransitoRespuesta actualizar(Long id, TransitoSolicitud solicitud, Usuario actor) {
        Transito transito = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(transito.getAnimal(), actor);
        validarFechas(solicitud);
        if (transito.getFechaFin() != null && solicitud.getFechaFin() == null) {
            validarCapacidad(transito.getEspacioTransito());
        }

        transito.setFechaInicio(solicitud.getFechaInicio());
        transito.setFechaFin(solicitud.getFechaFin());
        transito.setEstado(solicitud.getFechaFin() == null ? EstadoTransito.EN_CURSO : EstadoTransito.FINALIZADO);
        transito.setObservaciones(solicitud.getObservaciones());

        return aRespuesta(transitoRepository.save(transito));
    }

    public void eliminar(Long id, Usuario actor) {
        Transito transito = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(transito.getAnimal(), actor);
        transito.setActivo(false);
        transitoRepository.save(transito);
    }

    void cerrarEnCurso(Animal animal, LocalDate fecha, String evento) {
        List<Transito> enCurso = transitoRepository.findByAnimalIdAndFechaFinIsNullAndActivoTrue(animal.getId());
        for (Transito transito : enCurso) {
            if (fecha.isBefore(transito.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de la " + evento + " no puede ser anterior al inicio del tránsito en curso ("
                        + transito.getFechaInicio() + ")");
            }
        }
        for (Transito transito : enCurso) {
            transito.setFechaFin(fecha);
            transito.setEstado(EstadoTransito.FINALIZADO);
            transitoRepository.save(transito);
        }
    }

    private void validarCapacidad(EspacioTransito espacio) {
        long ocupacion = espacioTransitoService.ocupacion(espacio);
        if (ocupacion >= espacio.getCapacidad()) {
            throw new IllegalArgumentException("El espacio " + espacio.getNombre() + " alcanzó su capacidad máxima ("
                    + espacio.getCapacidad() + "). No se puede registrar el ingreso");
        }
    }

    private void validarFechas(TransitoSolicitud solicitud) {
        if (solicitud.getFechaFin() != null && solicitud.getFechaFin().isBefore(solicitud.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private Transito buscarEntidad(Long id) {
        return transitoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tránsito no encontrado: " + id));
    }

    private TransitoRespuesta aRespuesta(Transito transito) {
        return new TransitoRespuesta(
                transito.getId(),
                transito.getAnimal().getId(),
                transito.getAnimal().getNombre(),
                transito.getEspacioTransito().getId(),
                transito.getEspacioTransito().getNombre(),
                transito.getEspacioTransito().getOrganizacion().getId(),
                transito.getEspacioTransito().getOrganizacion().getNombre(),
                transito.getFechaInicio(),
                transito.getFechaFin(),
                transito.getEstado(),
                transito.getObservaciones()
        );
    }
}
