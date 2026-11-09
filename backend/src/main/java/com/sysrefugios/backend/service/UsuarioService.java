package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.UsuarioSolicitud;
import com.sysrefugios.backend.dto.UsuarioRespuesta;
import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.EstadoUsuario;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import com.sysrefugios.backend.repository.UsuarioRepository;
import com.sysrefugios.backend.util.GeneradorHashContrasena;
import com.sysrefugios.backend.util.PoliticaContrasena;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionRepository organizacionRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, OrganizacionRepository organizacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.organizacionRepository = organizacionRepository;
    }

    public List<UsuarioRespuesta> listar(Usuario actor) {
        return usuarioRepository.findByOrganizacionIdAndEstadoNot(actor.getOrganizacion().getId(), EstadoUsuario.BAJA).stream()
                .map(UsuarioRespuesta::desde)
                .toList();
    }

    public UsuarioRespuesta buscarPorId(Long id) {
        return UsuarioRespuesta.desde(buscarEntidad(id));
    }

    public UsuarioRespuesta crear(UsuarioSolicitud solicitud, Usuario actor) {
        Organizacion organizacion = buscarOrganizacion(solicitud.getOrganizacionId());
        exigirMismaOrganizacion(actor, organizacion);

        if (solicitud.getContrasena() == null || solicitud.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña provisoria es obligatoria al crear un usuario");
        }
        if (usuarioRepository.findByEmail(solicitud.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(solicitud.getNombre());
        usuario.setApellido(solicitud.getApellido());
        usuario.setEmail(solicitud.getEmail());
        usuario.setOrganizacion(organizacion);
        asignarContrasenaProvisoria(usuario, solicitud.getContrasena());

        return UsuarioRespuesta.desde(usuarioRepository.save(usuario));
    }

    public UsuarioRespuesta actualizar(Long id, UsuarioSolicitud solicitud, Usuario actor) {
        Usuario usuario = buscarEntidad(id);
        exigirMismaOrganizacion(actor, usuario.getOrganizacion());
        if (!usuario.getOrganizacion().getId().equals(solicitud.getOrganizacionId())) {
            throw new IllegalArgumentException("No se puede cambiar la organización de un usuario");
        }

        usuarioRepository.findByEmail(solicitud.getEmail())
                .filter(otro -> !otro.getId().equals(usuario.getId()))
                .ifPresent(otro -> {
                    throw new IllegalArgumentException("Ya existe un usuario con ese email");
                });
        usuario.setNombre(solicitud.getNombre());
        usuario.setApellido(solicitud.getApellido());
        usuario.setEmail(solicitud.getEmail());
        if (solicitud.getContrasena() != null && !solicitud.getContrasena().isBlank()) {
            asignarContrasenaProvisoria(usuario, solicitud.getContrasena());
        }

        return UsuarioRespuesta.desde(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id, Usuario actor) {
        Usuario usuario = buscarEntidad(id);
        exigirMismaOrganizacion(actor, usuario.getOrganizacion());
        if (usuario.getId().equals(actor.getId())) {
            throw new IllegalArgumentException("Un usuario no puede darse de baja a sí mismo");
        }
        usuario.setEstado(EstadoUsuario.BAJA);
        usuarioRepository.save(usuario);
    }

    private void asignarContrasenaProvisoria(Usuario usuario, String contrasena) {
        PoliticaContrasena.validar(contrasena);
        usuario.setContrasena(GeneradorHashContrasena.generar(contrasena));
        usuario.setCambioObligatorio(true);
        usuario.setIntentosFallidos(0);
        usuario.setEstado(EstadoUsuario.ACTIVO);
    }

    private void exigirMismaOrganizacion(Usuario actor, Organizacion organizacion) {
        if (!actor.getOrganizacion().getId().equals(organizacion.getId())) {
            throw new AccesoDenegadoException("OTRA_ORGANIZACION",
                    "Solo un usuario de " + organizacion.getNombre() + " puede dar de alta, desbloquear o dar de baja "
                            + "usuarios de esa organización");
        }
    }

    private Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> usuario.getEstado() != EstadoUsuario.BAJA)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private Organizacion buscarOrganizacion(Long organizacionId) {
        return organizacionRepository.findByIdAndActivoTrue(organizacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + organizacionId));
    }
}
