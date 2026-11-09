package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.CastracionSolicitud;
import com.sysrefugios.backend.dto.CastracionRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.Castracion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.CastracionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CastracionService {

    private final CastracionRepository castracionRepository;
    private final AnimalService animalService;
    private final AlertaCastracionService alertaCastracionService;

    public CastracionService(CastracionRepository castracionRepository, AnimalService animalService,
                             AlertaCastracionService alertaCastracionService) {
        this.castracionRepository = castracionRepository;
        this.animalService = animalService;
        this.alertaCastracionService = alertaCastracionService;
    }

    public List<CastracionRespuesta> listar(Long animalId) {
        List<Castracion> registros = animalId != null
                ? castracionRepository.findByAnimalIdAndActivoTrue(animalId)
                : castracionRepository.findByActivoTrue();
        return registros.stream().map(this::aRespuesta).toList();
    }

    public CastracionRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public CastracionRespuesta crear(CastracionSolicitud solicitud, Usuario actor) {
        Animal animal = animalService.buscarEntidad(solicitud.getAnimalId());
        ControlAcceso.exigirAnimalACargo(animal, actor);

        Castracion castracion = new Castracion();
        castracion.setAnimal(animal);
        castracion.setFecha(solicitud.getFecha());
        castracion.setVeterinario(solicitud.getVeterinario());
        castracion.setObservaciones(solicitud.getObservaciones());

        castracion = castracionRepository.save(castracion);
        alertaCastracionService.actualizarPorAnimal(animal.getId());
        return aRespuesta(castracion);
    }

    public CastracionRespuesta actualizar(Long id, CastracionSolicitud solicitud, Usuario actor) {
        Castracion castracion = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(castracion.getAnimal(), actor);

        castracion.setFecha(solicitud.getFecha());
        castracion.setVeterinario(solicitud.getVeterinario());
        castracion.setObservaciones(solicitud.getObservaciones());

        return aRespuesta(castracionRepository.save(castracion));
    }

    public void eliminar(Long id, Usuario actor) {
        Castracion castracion = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(castracion.getAnimal(), actor);
        castracion.setActivo(false);
        castracionRepository.save(castracion);
        alertaCastracionService.actualizarPorAnimal(castracion.getAnimal().getId());
    }

    private Castracion buscarEntidad(Long id) {
        return castracionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Castración no encontrada: " + id));
    }

    private CastracionRespuesta aRespuesta(Castracion castracion) {
        return new CastracionRespuesta(
                castracion.getId(),
                castracion.getAnimal().getId(),
                castracion.getAnimal().getNombre(),
                castracion.getFecha(),
                castracion.getVeterinario(),
                castracion.getObservaciones()
        );
    }
}
