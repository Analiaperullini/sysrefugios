package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoAsignacion;

import java.time.LocalDate;

public class AsignacionCastracionRespuesta {

    private Long id;

    private Long campaniaId;

    private String campaniaNombre;

    private Long animalId;

    private String animalNombre;

    private String animalOrganizacionNombre;

    private LocalDate fechaAsignacion;

    private EstadoAsignacion estado;

    private String observaciones;

    public AsignacionCastracionRespuesta() {
    }

    public AsignacionCastracionRespuesta(Long id, Long campaniaId, String campaniaNombre, Long animalId, String animalNombre, String animalOrganizacionNombre, LocalDate fechaAsignacion, EstadoAsignacion estado, String observaciones) {
        this.id = id;
        this.campaniaId = campaniaId;
        this.campaniaNombre = campaniaNombre;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.animalOrganizacionNombre = animalOrganizacionNombre;
        this.fechaAsignacion = fechaAsignacion;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampaniaId() {
        return campaniaId;
    }

    public void setCampaniaId(Long campaniaId) {
        this.campaniaId = campaniaId;
    }

    public String getCampaniaNombre() {
        return campaniaNombre;
    }

    public void setCampaniaNombre(String campaniaNombre) {
        this.campaniaNombre = campaniaNombre;
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

    public String getAnimalOrganizacionNombre() {
        return animalOrganizacionNombre;
    }

    public void setAnimalOrganizacionNombre(String animalOrganizacionNombre) {
        this.animalOrganizacionNombre = animalOrganizacionNombre;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoAsignacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignacion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
