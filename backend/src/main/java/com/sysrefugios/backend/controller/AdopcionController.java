package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.AdopcionSolicitud;
import com.sysrefugios.backend.dto.AdopcionRespuesta;
import com.sysrefugios.backend.service.AdopcionService;
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
@RequestMapping("/api/adopciones")
public class AdopcionController {

    private final AdopcionService adopcionService;

    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    @GetMapping
    public List<AdopcionRespuesta> listar(@RequestParam(required = false) Long animalId, HttpServletRequest solicitudHttp) {
        return adopcionService.listar(animalId, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @GetMapping("/alertas-castracion")
    public List<AdopcionRespuesta> listarAlertasCastracion(HttpServletRequest solicitudHttp) {
        return adopcionService.listarAlertasCastracion(SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @GetMapping("/{id}")
    public AdopcionRespuesta buscarPorId(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        return adopcionService.buscarPorId(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdopcionRespuesta crear(@Valid @RequestBody AdopcionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return adopcionService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public AdopcionRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody AdopcionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return adopcionService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        adopcionService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
