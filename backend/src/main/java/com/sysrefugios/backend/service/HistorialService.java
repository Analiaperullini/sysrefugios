package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.EventoHistorialRespuesta;
import com.sysrefugios.backend.dto.HistorialRespuesta;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Adopcion;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.AsignacionCastracion;
import com.sysrefugios.backend.model.CampaniaCastracion;
import com.sysrefugios.backend.model.Castracion;
import com.sysrefugios.backend.model.Derivacion;
import com.sysrefugios.backend.model.EstadoAsignacion;
import com.sysrefugios.backend.model.Rescate;
import com.sysrefugios.backend.model.Seguimiento;
import com.sysrefugios.backend.model.Transito;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.model.Vacunacion;
import com.sysrefugios.backend.repository.AdopcionRepository;
import com.sysrefugios.backend.repository.AnimalRepository;
import com.sysrefugios.backend.repository.AsignacionCastracionRepository;
import com.sysrefugios.backend.repository.CastracionRepository;
import com.sysrefugios.backend.repository.DerivacionRepository;
import com.sysrefugios.backend.repository.RescateRepository;
import com.sysrefugios.backend.repository.SeguimientoRepository;
import com.sysrefugios.backend.repository.TransitoRepository;
import com.sysrefugios.backend.repository.VacunacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class HistorialService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final List<String> ORDEN_TIPOS = List.of(
            "RESCATE", "TRANSITO", "DERIVACION", "VACUNACION", "CAMPANIA", "CASTRACION", "ADOPCION", "SEGUIMIENTO");

    private final AnimalRepository animalRepository;
    private final AnimalService animalService;
    private final RescateRepository rescateRepository;
    private final TransitoRepository transitoRepository;
    private final DerivacionRepository derivacionRepository;
    private final AdopcionRepository adopcionRepository;
    private final SeguimientoRepository seguimientoRepository;
    private final VacunacionRepository vacunacionRepository;
    private final CastracionRepository castracionRepository;
    private final AsignacionCastracionRepository asignacionCastracionRepository;

    public HistorialService(AnimalRepository animalRepository, AnimalService animalService,
                            RescateRepository rescateRepository, TransitoRepository transitoRepository,
                            DerivacionRepository derivacionRepository, AdopcionRepository adopcionRepository,
                            SeguimientoRepository seguimientoRepository, VacunacionRepository vacunacionRepository,
                            CastracionRepository castracionRepository,
                            AsignacionCastracionRepository asignacionCastracionRepository) {
        this.animalRepository = animalRepository;
        this.animalService = animalService;
        this.rescateRepository = rescateRepository;
        this.transitoRepository = transitoRepository;
        this.derivacionRepository = derivacionRepository;
        this.adopcionRepository = adopcionRepository;
        this.seguimientoRepository = seguimientoRepository;
        this.vacunacionRepository = vacunacionRepository;
        this.castracionRepository = castracionRepository;
        this.asignacionCastracionRepository = asignacionCastracionRepository;
    }

    public HistorialRespuesta obtener(Long animalId, Usuario actor) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Animal no encontrado: " + animalId));

        List<EventoHistorialRespuesta> eventos = new ArrayList<>();
        rescateRepository.findByAnimalId(animalId).forEach(rescate -> eventos.add(evento(rescate)));
        transitoRepository.findByAnimalId(animalId).forEach(transito -> eventos.add(evento(transito)));
        derivacionRepository.findByAnimalId(animalId).forEach(derivacion -> eventos.add(evento(derivacion)));
        vacunacionRepository.findByAnimalId(animalId).forEach(vacunacion -> eventos.add(evento(vacunacion)));
        castracionRepository.findByAnimalId(animalId).forEach(castracion -> eventos.add(evento(castracion)));
        asignacionCastracionRepository.findByAnimalId(animalId).forEach(asignacion -> eventos.add(evento(asignacion)));
        adopcionRepository.findByAnimalId(animalId).forEach(adopcion -> eventos.add(evento(adopcion, actor)));
        seguimientoRepository.findByAdopcionAnimalId(animalId).forEach(seguimiento -> eventos.add(evento(seguimiento)));

        Set<String> llegadasPorDerivacion = new HashSet<>();
        derivacionRepository.findByAnimalId(animalId).forEach(derivacion -> llegadasPorDerivacion.add(
                claveLlegada(derivacion.getFechaDerivacion(), derivacion.getOrganizacionDestino().getNombre())));

        eventos.sort(Comparator.comparing(EventoHistorialRespuesta::getFecha)
                .thenComparing(evento -> ordenDelDia(evento, llegadasPorDerivacion))
                .thenComparing(EventoHistorialRespuesta::getRegistroId));

        return new HistorialRespuesta(animalService.aRespuesta(animal), eventos);
    }

    private double ordenDelDia(EventoHistorialRespuesta evento, Set<String> llegadasPorDerivacion) {
        int orden = ORDEN_TIPOS.indexOf(evento.getTipo());
        boolean llegaPorDerivacion = "TRANSITO".equals(evento.getTipo())
                && llegadasPorDerivacion.contains(claveLlegada(evento.getFecha(), evento.getOrganizacionNombre()));
        return llegaPorDerivacion ? ORDEN_TIPOS.indexOf("DERIVACION") + 0.5 : orden;
    }

    private String claveLlegada(LocalDate fecha, String organizacion) {
        return fecha + "|" + organizacion;
    }

    private String fecha(LocalDate fecha) {
        return fecha.format(FORMATO_FECHA);
    }

    private EventoHistorialRespuesta evento(Rescate rescate) {
        List<String> detalles = new ArrayList<>();
        detalles.add("Estado del rescate: " + legible(rescate.getEstado()));
        detalles.add("Responsable: " + rescate.getUsuario().nombreCompleto());
        agregarSiHay(detalles, "Observaciones", rescate.getObservaciones());
        agregarSiHay(detalles, "Descripción", rescate.getDescripcion());
        String lugar = rescate.getLugar() != null ? " en " + rescate.getLugar() : "";
        return new EventoHistorialRespuesta("RESCATE", rescate.getFechaRescate(), rescate.getId(), rescate.isActivo(),
                rescate.getOrganizacion().getNombre(), "Rescate" + lugar, detalles);
    }

    private EventoHistorialRespuesta evento(Transito transito) {
        List<String> detalles = new ArrayList<>();
        detalles.add(transito.getFechaFin() != null ? "Hasta: " + fecha(transito.getFechaFin()) : "Estadía en curso");
        agregarSiHay(detalles, "Observaciones", transito.getObservaciones());
        return new EventoHistorialRespuesta("TRANSITO", transito.getFechaInicio(), transito.getId(), transito.isActivo(),
                transito.getEspacioTransito().getOrganizacion().getNombre(),
                "Ingreso a tránsito: " + transito.getEspacioTransito().getNombre(), detalles);
    }

    private EventoHistorialRespuesta evento(Derivacion derivacion) {
        List<String> detalles = new ArrayList<>();
        detalles.add("Motivo: " + derivacion.getMotivo());
        agregarSiHay(detalles, "Observaciones", derivacion.getObservaciones());
        return new EventoHistorialRespuesta("DERIVACION", derivacion.getFechaDerivacion(), derivacion.getId(), derivacion.isActivo(),
                derivacion.getOrganizacionOrigen().getNombre(),
                "Derivación de " + derivacion.getOrganizacionOrigen().getNombre()
                        + " a " + derivacion.getOrganizacionDestino().getNombre(), detalles);
    }

    private EventoHistorialRespuesta evento(Vacunacion vacunacion) {
        List<String> detalles = new ArrayList<>();
        detalles.add("Dosis: " + vacunacion.getDosis());
        agregarSiHay(detalles, "Observaciones", vacunacion.getObservaciones());
        return new EventoHistorialRespuesta("VACUNACION", vacunacion.getFecha(), vacunacion.getId(), vacunacion.isActivo(),
                vacunacion.getAnimal().getOrganizacion().getNombre(), "Vacuna: " + vacunacion.getVacuna(), detalles);
    }

    private EventoHistorialRespuesta evento(Castracion castracion) {
        List<String> detalles = new ArrayList<>();
        detalles.add("Veterinario: " + castracion.getVeterinario());
        agregarSiHay(detalles, "Observaciones", castracion.getObservaciones());
        return new EventoHistorialRespuesta("CASTRACION", castracion.getFecha(), castracion.getId(), castracion.isActivo(),
                castracion.getAnimal().getOrganizacion().getNombre(), "Castración", detalles);
    }

    private EventoHistorialRespuesta evento(AsignacionCastracion asignacion) {
        CampaniaCastracion campania = asignacion.getCampania();
        List<String> detalles = new ArrayList<>();
        detalles.add("Fecha de la campaña: " + fecha(campania.getFecha()));
        detalles.add("Estado de la asignación: " + legible(asignacion.getEstado()));
        agregarSiHay(detalles, "Lugar", campania.getLugar());
        agregarSiHay(detalles, "Observaciones", asignacion.getObservaciones());
        return new EventoHistorialRespuesta("CAMPANIA", asignacion.getFechaAsignacion(), asignacion.getId(),
                asignacion.getEstado() != EstadoAsignacion.CANCELADA, campania.getOrganizacion().getNombre(),
                "Asignado a la campaña de castración: " + campania.getNombre(), detalles);
    }

    private EventoHistorialRespuesta evento(Adopcion adopcion, Usuario actor) {
        boolean mismaOrganizacion = ControlAcceso.esDeLaOrganizacion(adopcion.getOrganizacion(), actor);
        List<String> detalles = new ArrayList<>();
        detalles.add("Adoptante: " + (mismaOrganizacion
                ? adopcion.getAdoptante().nombreCompleto()
                : "datos reservados a " + adopcion.getOrganizacion().getNombre()));
        detalles.add("Requisitos: mayor de edad " + siNo(adopcion.isCumpleMayorEdad())
                + ", responsabilidad " + siNo(adopcion.isCumpleResponsabilidad())
                + ", espacio cerrado " + siNo(adopcion.isCumpleEspacioCerrado()));
        detalles.add("Compromiso de vacunación: " + siNo(adopcion.isCompromisoVacunacion()));
        detalles.add("Compromiso de castración: " + siNo(adopcion.isCompromisoCastracion())
                + " (fecha límite " + fecha(adopcion.getFechaLimiteCastracion()) + ")");
        if (adopcion.isAlertaCastracionVencida()) {
            detalles.add("ALERTA: venció el plazo de castración sin castración registrada");
        }
        agregarSiHay(detalles, "Condiciones", adopcion.getCondiciones());
        agregarSiHay(detalles, "Observaciones", adopcion.getObservaciones());
        return new EventoHistorialRespuesta("ADOPCION", adopcion.getFechaAdopcion(), adopcion.getId(), adopcion.isActivo(),
                adopcion.getOrganizacion().getNombre(), "Adopción", detalles);
    }

    private EventoHistorialRespuesta evento(Seguimiento seguimiento) {
        List<String> detalles = new ArrayList<>();
        detalles.add("Estado del animal: " + seguimiento.getEstadoAnimal());
        agregarSiHay(detalles, "Observaciones", seguimiento.getObservaciones());
        agregarSiHay(detalles, "Referencia multimedia", seguimiento.getReferenciaMultimedia());
        return new EventoHistorialRespuesta("SEGUIMIENTO", seguimiento.getFechaSeguimiento(), seguimiento.getId(),
                seguimiento.isActivo(), seguimiento.getAdopcion().getOrganizacion().getNombre(),
                "Seguimiento de la adopción", detalles);
    }

    private void agregarSiHay(List<String> detalles, String etiqueta, String valor) {
        if (valor != null && !valor.isBlank()) {
            detalles.add(etiqueta + ": " + valor);
        }
    }

    private String legible(Enum<?> valor) {
        String texto = valor.name().replace('_', ' ').toLowerCase(Locale.ROOT);
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private String siNo(boolean valor) {
        return valor ? "sí" : "no";
    }
}
