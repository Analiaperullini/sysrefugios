package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoTransito;

import java.time.LocalDate;

public class TransitoRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private Long espacioTransitoId;

    private String espacioTransitoNombre;

    private Long organizacionId;

    private String organizacionNombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private EstadoTransito estado;

    private String observaciones;

    public TransitoRespuesta() {
    }

    public TransitoRespuesta(Long id, Long animalId, String animalNombre, Long espacioTransitoId, String espacioTransitoNombre, Long organizacionId, String organizacionNombre, LocalDate fechaInicio, LocalDate fechaFin, EstadoTransito estado, String observaciones) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.espacioTransitoId = espacioTransitoId;
        this.espacioTransitoNombre = espacioTransitoNombre;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getAnimalNombre() {
        return animalNombre;
    }

    public void setAnimalNombre(String animalNombre) {
        this.animalNombre = animalNombre;
    }

    public Long getEspacioTransitoId() {
        return espacioTransitoId;
    }

    public void setEspacioTransitoId(Long espacioTransitoId) {
        this.espacioTransitoId = espacioTransitoId;
    }

    public String getEspacioTransitoNombre() {
        return espacioTransitoNombre;
    }

    public void setEspacioTransitoNombre(String espacioTransitoNombre) {
        this.espacioTransitoNombre = espacioTransitoNombre;
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }

    public String getOrganizacionNombre() {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre) {
        this.organizacionNombre = organizacionNombre;
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

    public EstadoTransito getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransito estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
