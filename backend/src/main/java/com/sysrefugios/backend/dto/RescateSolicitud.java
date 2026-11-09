package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoRescate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RescateSolicitud {

    private Long animalId;

    @Valid
    private AnimalIngresoSolicitud animal;

    private Long espacioTransitoId;

    @NotNull
    private LocalDate fechaRescate;

    private String lugar;

    private String descripcion;

    private String observaciones;

    @NotNull
    private Long organizacionId;

    private EstadoRescate estado;

    private Boolean confirmaAlimentacionAutonoma;

    public RescateSolicitud() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public AnimalIngresoSolicitud getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalIngresoSolicitud animal) {
        this.animal = animal;
    }

    public Long getEspacioTransitoId() {
        return espacioTransitoId;
    }

    public void setEspacioTransitoId(Long espacioTransitoId) {
        this.espacioTransitoId = espacioTransitoId;
    }

    public LocalDate getFechaRescate() {
        return fechaRescate;
    }

    public void setFechaRescate(LocalDate fechaRescate) {
        this.fechaRescate = fechaRescate;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }

    public EstadoRescate getEstado() {
        return estado;
    }

    public void setEstado(EstadoRescate estado) {
        this.estado = estado;
    }

    public Boolean getConfirmaAlimentacionAutonoma() {
        return confirmaAlimentacionAutonoma;
    }

    public void setConfirmaAlimentacionAutonoma(Boolean confirmaAlimentacionAutonoma) {
        this.confirmaAlimentacionAutonoma = confirmaAlimentacionAutonoma;
    }
}
