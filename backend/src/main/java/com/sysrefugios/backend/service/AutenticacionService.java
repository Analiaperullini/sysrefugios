package com.sysrefugios.backend.service;

import com.sysrefugios.backend.dto.CambioContrasenaSolicitud;
import com.sysrefugios.backend.dto.IngresoSolicitud;
import com.sysrefugios.backend.dto.UsuarioRespuesta;
import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.exception.CredencialesInvalidasException;
import com.sysrefugios.backend.exception.NoAutenticadoException;
import com.sysrefugios.backend.model.EstadoUsuario;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.UsuarioRepository;
import com.sysrefugios.backend.util.GeneradorHashContrasena;
import com.sysrefugios.backend.util.PoliticaContrasena;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AutenticacionService {

    public static final int MAX_INTENTOS_FALLIDOS = 5;
    public static final int DIAS_VIGENCIA_CONTRASENA = 180;
    public static final String ATRIBUTO_USUARIO_ID = "usuarioId";

    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioRespuesta ingresar(IngresoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        Usuario usuario = usuarioRepository.findByEmail(solicitud.getEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("Email o contraseña incorrectos"));

        if (usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw usuarioBloqueado();
        }

        boolean contrasenaCorrecta = GeneradorHashContrasena.coincide(solicitud.getContrasena(), usuario.getContrasena());

        if (usuario.getEstado() == EstadoUsuario.BAJA) {
            if (contrasenaCorrecta) {
                throw new AccesoDenegadoException("USUARIO_BAJA", "El usuario está dado de baja y no puede ingresar");
            }
            throw new CredencialesInvalidasException("Email o contraseña incorrectos");
        }

        if (!contrasenaCorrecta) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() >= MAX_INTENTOS_FALLIDOS) {
                usuario.setEstado(EstadoUsuario.BLOQUEADO);
            }
            usuarioRepository.save(usuario);
            if (usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
                throw usuarioBloqueado();
            }
            throw new CredencialesInvalidasException("Email o contraseña incorrectos");
        }

        usuario.setIntentosFallidos(0);
        if (contrasenaVencida(usuario)) {
            usuario.setCambioObligatorio(true);
        }
        usuarioRepository.save(usuario);

        HttpSession anterior = solicitudHttp.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        solicitudHttp.getSession(true).setAttribute(ATRIBUTO_USUARIO_ID, usuario.getId());

        return UsuarioRespuesta.desde(usuario);
    }

    public void salir(HttpServletRequest solicitudHttp) {
        HttpSession sesion = solicitudHttp.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
    }

    public Usuario usuarioDeSesion(HttpServletRequest solicitudHttp) {
        HttpSession sesion = solicitudHttp.getSession(false);
        Object usuarioId = sesion == null ? null : sesion.getAttribute(ATRIBUTO_USUARIO_ID);
        if (usuarioId == null) {
            throw new NoAutenticadoException("La sesión no está iniciada o venció por inactividad");
        }

        Usuario usuario = usuarioRepository.findById((Long) usuarioId).orElse(null);
        if (usuario == null || usuario.getEstado() != EstadoUsuario.ACTIVO) {
            sesion.invalidate();
            throw new NoAutenticadoException("La sesión ya no es válida. Vuelva a ingresar");
        }
        return usuario;
    }

    public UsuarioRespuesta cambiarContrasena(Usuario usuario, CambioContrasenaSolicitud solicitud) {
        if (!GeneradorHashContrasena.coincide(solicitud.getContrasenaActual(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        if (solicitud.getContrasenaActual().equals(solicitud.getContrasenaNueva())) {
            throw new IllegalArgumentException("La contraseña nueva debe ser distinta de la actual");
        }
        PoliticaContrasena.validar(solicitud.getContrasenaNueva());

        usuario.setContrasena(GeneradorHashContrasena.generar(solicitud.getContrasenaNueva()));
        usuario.setCambioObligatorio(false);
        usuario.setFechaCambioContrasena(LocalDateTime.now());
        return UsuarioRespuesta.desde(usuarioRepository.save(usuario));
    }

    private boolean contrasenaVencida(Usuario usuario) {
        LocalDateTime ultimoCambio = usuario.getFechaCambioContrasena();
        return ultimoCambio == null
                || ultimoCambio.isBefore(LocalDateTime.now().minusDays(DIAS_VIGENCIA_CONTRASENA));
    }

    private AccesoDenegadoException usuarioBloqueado() {
        return new AccesoDenegadoException("USUARIO_BLOQUEADO",
                "Usuario bloqueado por " + MAX_INTENTOS_FALLIDOS + " intentos fallidos consecutivos. "
                        + "Otro usuario de su organización debe asignarle una contraseña provisoria nueva");
    }
}
