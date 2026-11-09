package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.AdoptanteSolicitud;
import com.sysrefugios.backend.dto.AdoptanteRespuesta;
import com.sysrefugios.backend.service.AdoptanteService;
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
@RequestMapping("/api/adoptantes")
public class AdoptanteController {

    private final AdoptanteService adoptanteService;

    public AdoptanteController(AdoptanteService adoptanteService) {
        this.adoptanteService = adoptanteService;
    }

    @GetMapping
    public List<AdoptanteRespuesta> listar(HttpServletRequest solicitudHttp) {
        return adoptanteService.listar(SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @GetMapping("/{id}")
    public AdoptanteRespuesta buscarPorId(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        return adoptanteService.buscarPorId(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptanteRespuesta crear(@Valid @RequestBody AdoptanteSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return adoptanteService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public AdoptanteRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody AdoptanteSolicitud solicitud,
                                        HttpServletRequest solicitudHttp) {
        return adoptanteService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        adoptanteService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
