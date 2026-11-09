package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.AdoptanteSolicitud;
import com.sysrefugios.backend.dto.AdoptanteRespuesta;
import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Adoptante;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.AdoptanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdoptanteService {

    private final AdoptanteRepository adoptanteRepository;

    public AdoptanteService(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
    }

    public List<AdoptanteRespuesta> listar(Usuario actor) {
        return adoptanteRepository.findByOrganizacionIdAndActivoTrue(actor.getOrganizacion().getId()).stream()
                .map(this::aRespuesta)
                .toList();
    }

    public AdoptanteRespuesta buscarPorId(Long id, Usuario actor) {
        return aRespuesta(buscarEntidad(id, actor));
    }

    public AdoptanteRespuesta crear(AdoptanteSolicitud solicitud, Usuario actor) {
        validarConsentimiento(solicitud);
        validarDocumentoDisponible(solicitud.getDocumento(), actor.getOrganizacion().getId(), null);

        Adoptante adoptante = new Adoptante();
        adoptante.setOrganizacion(actor.getOrganizacion());
        adoptante.setConsentimientoDatos(true);
        adoptante.setFechaConsentimiento(LocalDate.now());
        aplicarDatos(adoptante, solicitud);

        return aRespuesta(adoptanteRepository.save(adoptante));
    }

    public AdoptanteRespuesta actualizar(Long id, AdoptanteSolicitud solicitud, Usuario actor) {
        Adoptante existente = buscarEntidad(id, actor);
        validarConsentimiento(solicitud);
        validarDocumentoDisponible(solicitud.getDocumento(), existente.getOrganizacion().getId(), id);

        aplicarDatos(existente, solicitud);
        return aRespuesta(adoptanteRepository.save(existente));
    }

    public void eliminar(Long id, Usuario actor) {
        Adoptante existente = buscarEntidad(id, actor);
        existente.setActivo(false);
        adoptanteRepository.save(existente);
    }

    Adoptante buscarEntidad(Long id, Usuario actor) {
        Adoptante adoptante = adoptanteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adoptante no encontrado: " + id));
        if (!adoptante.getOrganizacion().getId().equals(actor.getOrganizacion().getId())) {
            throw new AccesoDenegadoException("DATOS_RESERVADOS",
                    "Los datos personales del adoptante solo son visibles para "
                            + adoptante.getOrganizacion().getNombre());
        }
        return adoptante;
    }

    private void validarConsentimiento(AdoptanteSolicitud solicitud) {
        if (!Boolean.TRUE.equals(solicitud.getConsentimientoDatos())) {
            throw new IllegalArgumentException(
                    "Sin el consentimiento del adoptante no se pueden registrar sus datos personales");
        }
    }

    private void validarDocumentoDisponible(String documento, Long organizacionId, Long idAExcluir) {
        Optional<Adoptante> existente = adoptanteRepository.findByDocumentoAndOrganizacionId(documento, organizacionId);
        if (existente.isPresent() && !existente.get().getId().equals(idAExcluir)) {
            throw new IllegalArgumentException("Ya existe un adoptante registrado con ese documento");
        }
    }

    private void aplicarDatos(Adoptante adoptante, AdoptanteSolicitud solicitud) {
        adoptante.setNombre(solicitud.getNombre());
        adoptante.setApellido(solicitud.getApellido());
        adoptante.setDocumento(solicitud.getDocumento());
        adoptante.setTelefono(solicitud.getTelefono());
        adoptante.setDireccion(solicitud.getDireccion());
        adoptante.setEmail(solicitud.getEmail());
        adoptante.setObservaciones(solicitud.getObservaciones());
    }

    private AdoptanteRespuesta aRespuesta(Adoptante adoptante) {
        return new AdoptanteRespuesta(
                adoptante.getId(),
                adoptante.getNombre(),
                adoptante.getApellido(),
                adoptante.getDocumento(),
                adoptante.getTelefono(),
                adoptante.getEmail(),
                adoptante.getDireccion(),
                adoptante.getObservaciones(),
                adoptante.getOrganizacion().getId(),
                adoptante.getOrganizacion().getNombre(),
                adoptante.isConsentimientoDatos(),
                adoptante.getFechaConsentimiento()
        );
    }
}
