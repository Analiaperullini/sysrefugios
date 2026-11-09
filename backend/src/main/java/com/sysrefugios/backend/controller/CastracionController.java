package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.CastracionSolicitud;
import com.sysrefugios.backend.dto.CastracionRespuesta;
import com.sysrefugios.backend.service.CastracionService;
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
@RequestMapping("/api/castraciones")
public class CastracionController {

    private final CastracionService castracionService;

    public CastracionController(CastracionService castracionService) {
        this.castracionService = castracionService;
    }

    @GetMapping
    public List<CastracionRespuesta> listar(@RequestParam(required = false) Long animalId) {
        return castracionService.listar(animalId);
    }

    @GetMapping("/{id}")
    public CastracionRespuesta buscarPorId(@PathVariable Long id) {
        return castracionService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CastracionRespuesta crear(@Valid @RequestBody CastracionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return castracionService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public CastracionRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody CastracionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return castracionService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        castracionService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
