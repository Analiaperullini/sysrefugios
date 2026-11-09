package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.service.OrganizacionService;
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
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    private final OrganizacionService organizacionService;

    public OrganizacionController(OrganizacionService organizacionService) {
        this.organizacionService = organizacionService;
    }

    @GetMapping
    public List<Organizacion> listar() {
        return organizacionService.listar();
    }

    @GetMapping("/{id}")
    public Organizacion buscarPorId(@PathVariable Long id) {
        return organizacionService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organizacion crear(@Valid @RequestBody Organizacion organizacion) {
        return organizacionService.crear(organizacion);
    }

    @PutMapping("/{id}")
    public Organizacion actualizar(@PathVariable Long id, @Valid @RequestBody Organizacion organizacion, HttpServletRequest solicitudHttp) {
        return organizacionService.actualizar(id, organizacion, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        organizacionService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
