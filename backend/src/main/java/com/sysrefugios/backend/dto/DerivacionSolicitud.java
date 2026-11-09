package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DerivacionSolicitud {

    @NotNull
    private Long animalId;

    @NotNull
    private Long organizacionOrigenId;

    @NotNull
    private Long organizacionDestinoId;

    @NotNull
    private LocalDate fechaDerivacion;

    @NotBlank
    private String motivo;

    private String observaciones;

    public DerivacionSolicitud() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Long getOrganizacionOrigenId() {
        return organizacionOrigenId;
    }

    public void setOrganizacionOrigenId(Long organizacionOrigenId) {
        this.organizacionOrigenId = organizacionOrigenId;
    }

    public Long getOrganizacionDestinoId() {
        return organizacionDestinoId;
    }

    public void setOrganizacionDestinoId(Long organizacionDestinoId) {
        this.organizacionDestinoId = organizacionDestinoId;
    }

    public LocalDate getFechaDerivacion() {
        return fechaDerivacion;
    }

    public void setFechaDerivacion(LocalDate fechaDerivacion) {
        this.fechaDerivacion = fechaDerivacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
