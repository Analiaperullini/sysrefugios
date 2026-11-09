package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class CastracionRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private LocalDate fecha;

    private String veterinario;

    private String observaciones;

    public CastracionRespuesta() {
    }

    public CastracionRespuesta(Long id, Long animalId, String animalNombre, LocalDate fecha, String veterinario, String observaciones) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.fecha = fecha;
        this.veterinario = veterinario;
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

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
