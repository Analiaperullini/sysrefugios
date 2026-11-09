package com.sysrefugios.backend.controller;

import com.sysrefugios.backend.config.SesionInterceptor;
import com.sysrefugios.backend.dto.HistorialRespuesta;
import com.sysrefugios.backend.service.HistorialService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/api/animales/{id}/historial")
    public HistorialRespuesta obtener(@PathVariable Long id, HttpServletRequest solicitudHttp) {
        return historialService.obtener(id, SesionInterceptor.usuarioActual(solicitudHttp));
    }
}
