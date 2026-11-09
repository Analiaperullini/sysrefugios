package com.sysrefugios.backend.config;

import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.service.AutenticacionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SesionInterceptor implements HandlerInterceptor {

    private static final String ATRIBUTO_USUARIO = "usuarioActual";
    private static final Set<String> RUTAS_CON_CAMBIO_PENDIENTE = Set.of("/api/sesion", "/api/cambiar-contrasena");

    private final AutenticacionService autenticacionService;

    public SesionInterceptor(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest solicitud, HttpServletResponse respuesta, Object manejador) {
        if (CorsUtils.isPreFlightRequest(solicitud)) {
            return true;
        }

        Usuario usuario = autenticacionService.usuarioDeSesion(solicitud);
        if (usuario.isCambioObligatorio() && !RUTAS_CON_CAMBIO_PENDIENTE.contains(solicitud.getRequestURI())) {
            throw new AccesoDenegadoException("CAMBIO_OBLIGATORIO",
                    "Debe cambiar la contraseña (provisoria o vencida) antes de continuar");
        }

        solicitud.setAttribute(ATRIBUTO_USUARIO, usuario);
        return true;
    }

    public static Usuario usuarioActual(HttpServletRequest solicitud) {
        return (Usuario) solicitud.getAttribute(ATRIBUTO_USUARIO);
    }
}
