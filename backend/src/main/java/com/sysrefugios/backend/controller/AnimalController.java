package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.AnimalSolicitud;
import com.sysrefugios.backend.dto.AnimalRespuesta;
import com.sysrefugios.backend.service.AnimalService;
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
@RequestMapping("/api/animales")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalRespuesta> listar(@RequestParam(required = false) Long organizacionId,
                                       @RequestParam(required = false) String estado,
                                       @RequestParam(required = false) String nombre) {
        return animalService.listar(organizacionId, estado, nombre);
    }

    @GetMapping("/{id}")
    public AnimalRespuesta buscarPorId(@PathVariable Long id) {
        return animalService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalRespuesta crear(@Valid @RequestBody AnimalSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return animalService.crear(solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @PutMapping("/{id}")
    public AnimalRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody AnimalSolicitud solicitud, HttpServletRequest solicitudHttp) {
        return animalService.actualizar(id, solicitud, SesionInterceptor.usuarioActual(solicitudHttp));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        animalService.eliminar(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
