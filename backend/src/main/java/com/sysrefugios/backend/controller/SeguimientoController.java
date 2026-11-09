package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.SeguimientoSolicitud;
import com.sysrefugios.backend.dto.SeguimientoRespuesta;
import com.sysrefugios.backend.service.SeguimientoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @GetMapping
    public List<SeguimientoRespuesta> listar(@RequestParam(required = false) Long adopcionId, HttpServletRequest solicitudHttp) {
        return seguimientoService.listar(adopcionId, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @GetMapping("/{id}")
    public SeguimientoRespuesta buscarPorId(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        return seguimientoService.buscarPorId(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeguimientoRespuesta crear(@Valid @RequestBody SeguimientoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return seguimientoService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public SeguimientoRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody SeguimientoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return seguimientoService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        seguimientoService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
