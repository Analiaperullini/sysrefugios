package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.CambioContrasenaSolicitud;
import com.sysrefugios.backend.dto.IngresoSolicitud;
import com.sysrefugios.backend.dto.UsuarioRespuesta;
import com.sysrefugios.backend.service.AutenticacionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    public AutenticacionController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    public UsuarioRespuesta ingresar(@Valid @RequestBody IngresoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return autenticacionService.ingresar(solicitud, solicitudHttp);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void salir(HttpServletRequest solicitudHttp) {
        autenticacionService.salir(solicitudHttp);
    }

    @GetMapping("/sesion")
    public UsuarioRespuesta sesion(HttpServletRequest solicitudHttp) {
        return UsuarioRespuesta.desde(SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PostMapping("/cambiar-contrasena")
    public UsuarioRespuesta cambiarContrasena(@Valid @RequestBody CambioContrasenaSolicitud solicitud,
                                             HttpServletRequest solicitudHttp) {
        return autenticacionService.cambiarContrasena(SesionInterceptor.usuarioActual(solicitudHttp), solicitud);
    }
}
