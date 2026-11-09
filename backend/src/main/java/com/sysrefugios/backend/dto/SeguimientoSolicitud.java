package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class SeguimientoSolicitud {

    @NotNull
    private Long adopcionId;

    @NotNull
    private LocalDate fechaSeguimiento;

    @NotBlank
    @Size(max = 50)
    private String estadoAnimal;

    private String observaciones;

    private String referenciaMultimedia;

    private String informacionContacto;

    public SeguimientoSolicitud() {
    }

    public Long getAdopcionId() {
        return adopcionId;
    }

    public void setAdopcionId(Long adopcionId) {
        this.adopcionId = adopcionId;
    }

    public LocalDate getFechaSeguimiento() {
        return fechaSeguimiento;
    }

    public void setFechaSeguimiento(LocalDate fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }

    public String getEstadoAnimal() {
        return estadoAnimal;
    }

    public void setEstadoAnimal(String estadoAnimal) {
        this.estadoAnimal = estadoAnimal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getReferenciaMultimedia() {
        return referenciaMultimedia;
    }

    public void setReferenciaMultimedia(String referenciaMultimedia) {
        this.referenciaMultimedia = referenciaMultimedia;
    }

    public String getInformacionContacto() {
        return informacionContacto;
    }

    public void setInformacionContacto(String informacionContacto) {
        this.informacionContacto = informacionContacto;
    }
}
