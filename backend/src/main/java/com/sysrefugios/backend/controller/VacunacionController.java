package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.VacunacionSolicitud;
import com.sysrefugios.backend.dto.VacunacionRespuesta;
import com.sysrefugios.backend.service.VacunacionService;
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
@RequestMapping("/api/vacunaciones")
public class VacunacionController {

    private final VacunacionService vacunacionService;

    public VacunacionController(VacunacionService vacunacionService) {
        this.vacunacionService = vacunacionService;
    }

    @GetMapping
    public List<VacunacionRespuesta> listar(@RequestParam(required = false) Long animalId) {
        return vacunacionService.listar(animalId);
    }

    @GetMapping("/{id}")
    public VacunacionRespuesta buscarPorId(@PathVariable Long id) {
        return vacunacionService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacunacionRespuesta crear(@Valid @RequestBody VacunacionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return vacunacionService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public VacunacionRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody VacunacionSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return vacunacionService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        vacunacionService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
