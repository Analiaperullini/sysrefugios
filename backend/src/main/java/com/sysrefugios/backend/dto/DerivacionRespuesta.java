package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class DerivacionRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private Long organizacionOrigenId;

    private String organizacionOrigenNombre;

    private Long organizacionDestinoId;

    private String organizacionDestinoNombre;

    private LocalDate fechaDerivacion;

    private String motivo;

    private String observaciones;

    public DerivacionRespuesta() {
    }

    public DerivacionRespuesta(Long id, Long animalId, String animalNombre, Long organizacionOrigenId, String organizacionOrigenNombre, Long organizacionDestinoId, String organizacionDestinoNombre, LocalDate fechaDerivacion, String motivo, String observaciones) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.organizacionOrigenId = organizacionOrigenId;
        this.organizacionOrigenNombre = organizacionOrigenNombre;
        this.organizacionDestinoId = organizacionDestinoId;
        this.organizacionDestinoNombre = organizacionDestinoNombre;
        this.fechaDerivacion = fechaDerivacion;
        this.motivo = motivo;
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

    public Long getOrganizacionOrigenId() {
        return organizacionOrigenId;
    }

    public void setOrganizacionOrigenId(Long organizacionOrigenId) {
        this.organizacionOrigenId = organizacionOrigenId;
    }

    public String getOrganizacionOrigenNombre() {
        return organizacionOrigenNombre;
    }

    public void setOrganizacionOrigenNombre(String organizacionOrigenNombre) {
        this.organizacionOrigenNombre = organizacionOrigenNombre;
    }

    public Long getOrganizacionDestinoId() {
        return organizacionDestinoId;
    }

    public void setOrganizacionDestinoId(Long organizacionDestinoId) {
        this.organizacionDestinoId = organizacionDestinoId;
    }

    public String getOrganizacionDestinoNombre() {
        return organizacionDestinoNombre;
    }

    public void setOrganizacionDestinoNombre(String organizacionDestinoNombre) {
        this.organizacionDestinoNombre = organizacionDestinoNombre;
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
