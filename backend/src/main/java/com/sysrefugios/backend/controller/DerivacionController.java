package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.DerivacionSolicitud;
import com.sysrefugios.backend.dto.DerivacionRespuesta;
import com.sysrefugios.backend.service.DerivacionService;
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
@RequestMapping("/api/derivaciones")
public class DerivacionController {

    private final DerivacionService derivacionService;

    public DerivacionController(DerivacionService derivacionService) {
        this.derivacionService = derivacionService;
    }

    @GetMapping
    public List<DerivacionRespuesta> listar(@RequestParam(required = false) Long animalId) {
        return derivacionService.listar(animalId);
    }

    @GetMapping("/{id}")
    public DerivacionRespuesta buscarPorId(@PathVariable Long id) {
        return derivacionService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DerivacionRespuesta crear(@Valid @RequestBody DerivacionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return derivacionService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public DerivacionRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody DerivacionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return derivacionService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        derivacionService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
