package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoRescate;

import java.time.LocalDate;

public class RescateRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private LocalDate fechaRescate;

    private String lugar;

    private String descripcion;

    private String observaciones;

    private Long organizacionId;

    private String organizacionNombre;

    private EstadoRescate estado;

    private Long usuarioId;

    private String usuarioNombre;

    private String avisoDerivacion;

    public RescateRespuesta() {
    }

    public RescateRespuesta(Long id, Long animalId, String animalNombre, LocalDate fechaRescate, String lugar, String descripcion, String observaciones, Long organizacionId, String organizacionNombre, EstadoRescate estado, Long usuarioId, String usuarioNombre, String avisoDerivacion) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.fechaRescate = fechaRescate;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.estado = estado;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.avisoDerivacion = avisoDerivacion;
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

    public String getOrganizacionNombre() {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre) {
        this.organizacionNombre = organizacionNombre;
    }

    public EstadoRescate getEstado() {
        return estado;
    }

    public void setEstado(EstadoRescate estado) {
        this.estado = estado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getAvisoDerivacion() {
        return avisoDerivacion;
    }

    public void setAvisoDerivacion(String avisoDerivacion) {
        this.avisoDerivacion = avisoDerivacion;
    }
}
