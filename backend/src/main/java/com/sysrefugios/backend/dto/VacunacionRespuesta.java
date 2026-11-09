package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class VacunacionRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private LocalDate fecha;

    private String vacuna;

    private String dosis;

    private String observaciones;

    public VacunacionRespuesta() {
    }

    public VacunacionRespuesta(Long id, Long animalId, String animalNombre, LocalDate fecha, String vacuna, String dosis, String observaciones) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.fecha = fecha;
        this.vacuna = vacuna;
        this.dosis = dosis;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getVacuna() {
        return vacuna;
    }

    public void setVacuna(String vacuna) {
        this.vacuna = vacuna;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
