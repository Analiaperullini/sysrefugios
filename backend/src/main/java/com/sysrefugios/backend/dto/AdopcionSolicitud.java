package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AdopcionSolicitud {

    @NotNull
    private Long animalId;

    @NotNull
    private Long adoptanteId;

    @NotNull
    private LocalDate fechaAdopcion;

    @NotNull
    private Boolean cumpleMayorEdad;

    @NotNull
    private Boolean cumpleResponsabilidad;

    @NotNull
    private Boolean cumpleEspacioCerrado;

    @NotNull
    private Boolean compromisoVacunacion;

    @NotNull
    private Boolean compromisoCastracion;

    private String condiciones;

    private String observaciones;

    public AdopcionSolicitud() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public Boolean getCumpleMayorEdad() {
        return cumpleMayorEdad;
    }

    public void setCumpleMayorEdad(Boolean cumpleMayorEdad) {
        this.cumpleMayorEdad = cumpleMayorEdad;
    }

    public Boolean getCumpleResponsabilidad() {
        return cumpleResponsabilidad;
    }

    public void setCumpleResponsabilidad(Boolean cumpleResponsabilidad) {
        this.cumpleResponsabilidad = cumpleResponsabilidad;
    }

    public Boolean getCumpleEspacioCerrado() {
        return cumpleEspacioCerrado;
    }

    public void setCumpleEspacioCerrado(Boolean cumpleEspacioCerrado) {
        this.cumpleEspacioCerrado = cumpleEspacioCerrado;
    }

    public Boolean getCompromisoVacunacion() {
        return compromisoVacunacion;
    }

    public void setCompromisoVacunacion(Boolean compromisoVacunacion) {
        this.compromisoVacunacion = compromisoVacunacion;
    }

    public Boolean getCompromisoCastracion() {
        return compromisoCastracion;
    }

    public void setCompromisoCastracion(Boolean compromisoCastracion) {
        this.compromisoCastracion = compromisoCastracion;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
