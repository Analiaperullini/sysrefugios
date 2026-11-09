package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.VacunacionSolicitud;
import com.sysrefugios.backend.dto.VacunacionRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.model.Vacunacion;
import com.sysrefugios.backend.repository.VacunacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacunacionService {

    private final VacunacionRepository vacunacionRepository;
    private final AnimalService animalService;

    public VacunacionService(VacunacionRepository vacunacionRepository, AnimalService animalService) {
        this.vacunacionRepository = vacunacionRepository;
        this.animalService = animalService;
    }

    public List<VacunacionRespuesta> listar(Long animalId) {
        List<Vacunacion> registros = animalId != null
                ? vacunacionRepository.findByAnimalIdAndActivoTrue(animalId)
                : vacunacionRepository.findByActivoTrue();
        return registros.stream().map(this::aRespuesta).toList();
    }

    public VacunacionRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public VacunacionRespuesta crear(VacunacionSolicitud solicitud, Usuario actor) {
        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);

        Vacunacion vacunacion = new Vacunacion();
        vacunacion.setAnimal(animal);
        vacunacion.setFecha(solicitud.getFecha());
        vacunacion.setVacuna(solicitud.getVacuna());
        vacunacion.setDosis(solicitud.getDosis());
        vacunacion.setObservaciones(solicitud.getObservaciones());

        return aRespuesta(vacunacionRepository.save(vacunacion));
    }

    public VacunacionRespuesta actualizar(Long id, VacunacionSolicitud solicitud, Usuario actor) {
        Vacunacion vacunacion = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(vacunacion.getAnimal(), actor);

        vacunacion.setFecha(solicitud.getFecha());
        vacunacion.setVacuna(solicitud.getVacuna());
        vacunacion.setDosis(solicitud.getDosis());
        vacunacion.setObservaciones(solicitud.getObservaciones());

        return aRespuesta(vacunacionRepository.save(vacunacion));
    }

    public void eliminar(Long id, Usuario actor) {
        Vacunacion vacunacion = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(vacunacion.getAnimal(), actor);
        vacunacion.setActivo(false);
        vacunacionRepository.save(vacunacion);
    }

    private Vacunacion buscarEntidad(Long id) {
        return vacunacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vacunación no encontrada: " + id));
    }

    private VacunacionRespuesta aRespuesta(Vacunacion vacunacion) {
        return new VacunacionRespuesta(
                vacunacion.getId(),
                vacunacion.getAnimal().getId(),
                vacunacion.getAnimal().getNombre(),
                vacunacion.getFecha(),
                vacunacion.getVacuna(),
                vacunacion.getDosis(),
                vacunacion.getObservaciones()
        );
    }
}
