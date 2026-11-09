package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.AsignacionCastracionSolicitud;
import com.sysrefugios.backend.dto.AsignacionCastracionRespuesta;
import com.sysrefugios.backend.dto.CambioEstadoAsignacionSolicitud;
import com.sysrefugios.backend.dto.CampaniaCastracionSolicitud;
import com.sysrefugios.backend.dto.CampaniaCastracionRespuesta;
import com.sysrefugios.backend.service.CampaniaCastracionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CampaniaCastracionController {

    private final CampaniaCastracionService campaniaService;

    public CampaniaCastracionController(CampaniaCastracionService campaniaService) {
        this.campaniaService = campaniaService;
    }

    @GetMapping("/api/campanias-castracion")
    public List<CampaniaCastracionRespuesta> listar() {
        return campaniaService.listar();
    }

    @GetMapping("/api/campanias-castracion/{id}")
    public CampaniaCastracionRespuesta buscarPorId(@PathVariable Long id) {
        return campaniaService.buscarPorId(id);
    }

    @PostMapping("/api/campanias-castracion")
    @ResponseStatus(HttpStatus.CREATED)
    public CampaniaCastracionRespuesta crear(@Valid @RequestBody CampaniaCastracionSolicitud solicitud,
                                            HttpServletRequest solicitudHttp) {
        return campaniaService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/api/campanias-castracion/{id}")
    public CampaniaCastracionRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody CampaniaCastracionSolicitud solicitud,
                                                 HttpServletRequest solicitudHttp) {
        return campaniaService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/api/campanias-castracion/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        campaniaService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @GetMapping("/api/campanias-castracion/{id}/asignaciones")
    public List<AsignacionCastracionRespuesta> listarAsignaciones(@PathVariable Long id) {
        return campaniaService.listarAsignaciones(id);
    }

    @PostMapping("/api/campanias-castracion/{id}/asignaciones")
    @ResponseStatus(HttpStatus.CREATED)
    public AsignacionCastracionRespuesta asignar(@PathVariable Long id, @Valid @RequestBody AsignacionCastracionSolicitud solicitud,
                                                HttpServletRequest solicitudHttp) {
        return campaniaService.asignar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/api/asignaciones-castracion/{id}")
    public AsignacionCastracionRespuesta cambiarEstado(@PathVariable Long id,
                                                      @Valid @RequestBody CambioEstadoAsignacionSolicitud solicitud,
                                                      HttpServletRequest solicitudHttp) {
        return campaniaService.cambiarEstado(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
