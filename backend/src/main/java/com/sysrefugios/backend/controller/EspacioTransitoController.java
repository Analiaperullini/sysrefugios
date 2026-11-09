package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.EspacioTransitoSolicitud;
import com.sysrefugios.backend.dto.EspacioTransitoRespuesta;
import com.sysrefugios.backend.service.EspacioTransitoService;
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
@RequestMapping("/api/espacios-transito")
public class EspacioTransitoController {

    private final EspacioTransitoService espacioTransitoService;

    public EspacioTransitoController(EspacioTransitoService espacioTransitoService) {
        this.espacioTransitoService = espacioTransitoService;
    }

    @GetMapping
    public List<EspacioTransitoRespuesta> listar(@RequestParam(required = false) Long organizacionId) {
        return espacioTransitoService.listar(organizacionId);
    }

    @GetMapping("/{id}")
    public EspacioTransitoRespuesta buscarPorId(@PathVariable Long id) {
        return espacioTransitoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EspacioTransitoRespuesta crear(@Valid @RequestBody EspacioTransitoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return espacioTransitoService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public EspacioTransitoRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody EspacioTransitoSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return espacioTransitoService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        espacioTransitoService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
