package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.AnimalIngresoSolicitud;
import com.sysrefugios.backend.dto.AnimalSolicitud;
import com.sysrefugios.backend.dto.AnimalRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.AdopcionRepository;
import com.sysrefugios.backend.repository.AnimalRepository;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final OrganizacionRepository organizacionRepository;
    private final AdopcionRepository adopcionRepository;

    public AnimalService(AnimalRepository animalRepository, OrganizacionRepository organizacionRepository,
                         AdopcionRepository adopcionRepository) {
        this.animalRepository = animalRepository;
        this.organizacionRepository = organizacionRepository;
        this.adopcionRepository = adopcionRepository;
    }

    public List<AnimalRespuesta> listar(Long organizacionId, String estado, String nombre) {
        EstadoAnimal estadoBuscado = convertirEstado(estado);
        String texto = nombre == null || nombre.isBlank() ? null : nombre.trim().toLowerCase(Locale.ROOT);
        List<Animal> animales = organizacionId != null
                ? animalRepository.findByOrganizacionIdAndActivoTrue(organizacionId)
                : animalRepository.findByActivoTrue();
        Set<Long> conAlerta = adopcionRepository.findByAlertaCastracionVencidaTrueAndActivoTrue().stream()
                .map(adopcion -> adopcion.getAnimal().getId())
                .collect(Collectors.toSet());
        return animales.stream()
                .filter(animal -> estadoBuscado == null || animal.getEstado() == estadoBuscado)
                .filter(animal -> texto == null || animal.getNombre().toLowerCase(Locale.ROOT).contains(texto))
                .map(animal -> aRespuesta(animal, conAlerta.contains(animal.getId())))
                .toList();
    }

    private EstadoAnimal convertirEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        try {
            return EstadoAnimal.valueOf(estado.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de animal inválido: " + estado
                    + ". Valores aceptados: " + Arrays.toString(EstadoAnimal.values()));
        }
    }

    public AnimalRespuesta buscarPorId(Long id) {
        return aRespuesta(buscarEntidad(id));
    }

    public AnimalRespuesta crear(AnimalSolicitud solicitud, Usuario actor) {
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());
        ControlAcceso.exigirOrganizacionPropia(organizacion, actor, "El animal que se registra");
        Animal animal = new Animal();
        aplicarDatos(animal, solicitud, organizacion);
        return aRespuesta(animalRepository.save(animal));
    }

    public AnimalRespuesta actualizar(Long id, AnimalSolicitud solicitud, Usuario actor) {
        Animal animal = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(animal, actor);
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());
        if (!organizacion.getId().equals(animal.getOrganizacion().getId())) {
            throw new IllegalArgumentException(
                    "La organización a cargo del animal no se cambia desde la edición: hay que registrar una derivación");
        }
        aplicarDatos(animal, solicitud, organizacion);
        return aRespuesta(animalRepository.save(animal));
    }

    public void eliminar(Long id, Usuario actor) {
        Animal animal = buscarEntidad(id);
        ControlAcceso.exigirAnimalACargo(animal, actor);
        animal.setActivo(false);
        animalRepository.save(animal);
    }

    Animal buscarEntidad(Long id) {
        return animalRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Animal no encontrado: " + id));
    }

    Animal crearIngreso(AnimalIngresoSolicitud datos, Organizacion organizacion) {
        Animal animal = new Animal();
        animal.setNombre(datos.getNombre());
        animal.setEspecie(datos.getEspecie());
        animal.setSexo(datos.getSexo());
        animal.setFechaNacimiento(datos.getFechaNacimiento());
        animal.setColor(datos.getColor());
        animal.setEtapaVida(datos.getEtapaVida());
        animal.setTamanio(datos.getTamanio());
        animal.setDescripcion(datos.getDescripcion());
        animal.setEstado(EstadoAnimal.RESCATADO);
        animal.setOrganizacion(organizacion);
        return animalRepository.save(animal);
    }

    Animal guardar(Animal animal) {
        return animalRepository.save(animal);
    }

    String avisoDerivacion(Animal animal) {
        Organizacion actual = animal.getOrganizacion();
        if (actual.getEtapaVidaAtendida() == null || actual.getEtapaVidaAtendida() == animal.getEtapaVida()) {
            return null;
        }
        String destino = organizacionRepository.findFirstByEtapaVidaAtendidaAndActivoTrue(animal.getEtapaVida())
                .map(Organizacion::getNombre)
                .orElse("la organización que corresponda");
        return "El animal es " + animal.getEtapaVida().name().toLowerCase(Locale.ROOT) + " y " + actual.getNombre()
                + " recibe " + actual.getEtapaVidaAtendida().name().toLowerCase(Locale.ROOT) + "s: corresponde a " + destino
                + ", debe derivarse.";
    }

    private Organizacion buscarOrganizacion(Long organizacionId) {
        return organizacionRepository.findByIdAndActivoTrue(organizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + organizacionId));
    }

    private void aplicarDatos(Animal animal, AnimalSolicitud solicitud, Organizacion organizacion) {
        animal.setNombre(solicitud.getNombre());
        animal.setEspecie(solicitud.getEspecie());
        animal.setSexo(solicitud.getSexo());
        animal.setFechaNacimiento(solicitud.getFechaNacimiento());
        animal.setColor(solicitud.getColor());
        animal.setEtapaVida(solicitud.getEtapaVida());
        animal.setTamanio(solicitud.getTamanio());
        animal.setEstado(solicitud.getEstado());
        animal.setDescripcion(solicitud.getDescripcion());
        animal.setOrganizacion(organizacion);
    }

    AnimalRespuesta aRespuesta(Animal animal) {
        return aRespuesta(animal, adopcionRepository.existsByAnimalIdAndAlertaCastracionVencidaTrueAndActivoTrue(animal.getId()));
    }

    private AnimalRespuesta aRespuesta(Animal animal, boolean alertaCastracionVencida) {
        AnimalRespuesta respuesta = new AnimalRespuesta(
                animal.getId(),
                animal.getNombre(),
                animal.getEspecie(),
                animal.getSexo(),
                animal.getFechaNacimiento(),
                animal.getColor(),
                animal.getEtapaVida(),
                animal.getTamanio(),
                animal.getEstado(),
                animal.getDescripcion(),
                animal.getOrganizacion().getId(),
                animal.getOrganizacion().getNombre(),
                avisoDerivacion(animal)
        );
        respuesta.setAlertaCastracionVencida(alertaCastracionVencida);
        return respuesta;
    }
}
