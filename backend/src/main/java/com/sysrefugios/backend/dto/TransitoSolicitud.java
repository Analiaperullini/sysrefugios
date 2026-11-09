package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TransitoSolicitud {

    @NotNull
    private Long animalId;

    @NotNull
    private Long espacioTransitoId;

    @NotNull
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String observaciones;

    private Boolean confirmaAlimentacionAutonoma;

    public TransitoSolicitud() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Long getEspacioTransitoId() {
        return espacioTransitoId;
    }

    public void setEspacioTransitoId(Long espacioTransitoId) {
        this.espacioTransitoId = espacioTransitoId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getConfirmaAlimentacionAutonoma() {
        return confirmaAlimentacionAutonoma;
    }

    public void setConfirmaAlimentacionAutonoma(Boolean confirmaAlimentacionAutonoma) {
        this.confirmaAlimentacionAutonoma = confirmaAlimentacionAutonoma;
    }
}
