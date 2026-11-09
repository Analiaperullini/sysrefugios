package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class SeguimientoRespuesta {

    private Long id;

    private Long adopcionId;

    private String animalNombre;

    private String adoptanteNombre;

    private LocalDate fechaSeguimiento;

    private String estadoAnimal;

    private String observaciones;

    private String referenciaMultimedia;

    private String informacionContacto;

    public SeguimientoRespuesta() {
    }

    public SeguimientoRespuesta(Long id, Long adopcionId, String animalNombre, String adoptanteNombre, LocalDate fechaSeguimiento, String estadoAnimal, String observaciones, String referenciaMultimedia, String informacionContacto) {
        this.id = id;
        this.adopcionId = adopcionId;
        this.animalNombre = animalNombre;
        this.adoptanteNombre = adoptanteNombre;
        this.fechaSeguimiento = fechaSeguimiento;
        this.estadoAnimal = estadoAnimal;
        this.observaciones = observaciones;
        this.referenciaMultimedia = referenciaMultimedia;
        this.informacionContacto = informacionContacto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdopcionId() {
        return adopcionId;
    }

    public void setAdopcionId(Long adopcionId) {
        this.adopcionId = adopcionId;
    }

    public String getAnimalNombre() {
        return animalNombre;
    }

    public void setAnimalNombre(String animalNombre) {
        this.animalNombre = animalNombre;
    }

    public String getAdoptanteNombre() {
        return adoptanteNombre;
    }

    public void setAdoptanteNombre(String adoptanteNombre) {
        this.adoptanteNombre = adoptanteNombre;
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
