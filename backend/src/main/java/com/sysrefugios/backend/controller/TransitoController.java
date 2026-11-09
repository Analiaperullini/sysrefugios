package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.TransitoSolicitud;
import com.sysrefugios.backend.dto.TransitoRespuesta;
import com.sysrefugios.backend.service.TransitoService;
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
@RequestMapping("/api/transitos")
public class TransitoController {

    private final TransitoService transitoService;

    public TransitoController(TransitoService transitoService) {
        this.transitoService = transitoService;
    }

    @GetMapping
    public List<TransitoRespuesta> listar(@RequestParam(required = false) Long animalId) {
        return transitoService.listar(animalId);
    }

    @GetMapping("/{id}")
    public TransitoRespuesta buscarPorId(@PathVariable Long id) {
        return transitoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransitoRespuesta crear(@Valid @RequestBody TransitoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return transitoService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public TransitoRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody TransitoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return transitoService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        transitoService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
