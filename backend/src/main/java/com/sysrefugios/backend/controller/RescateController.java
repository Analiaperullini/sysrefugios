package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.RescateSolicitud;
import com.sysrefugios.backend.dto.RescateRespuesta;
import com.sysrefugios.backend.service.RescateService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rescates")
public class RescateController {

    private final RescateService rescateService;

    public RescateController(RescateService rescateService) {
        this.rescateService = rescateService;
    }

    @GetMapping
    public List<RescateRespuesta> listar() {
        return rescateService.listar();
    }

    @GetMapping("/{id}")
    public RescateRespuesta buscarPorId(@PathVariable Long id) {
        return rescateService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RescateRespuesta crear(@Valid @RequestBody RescateSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return rescateService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public RescateRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody RescateSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return rescateService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        rescateService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
