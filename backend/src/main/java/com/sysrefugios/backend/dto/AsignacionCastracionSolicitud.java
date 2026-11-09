package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotNull;

public class AsignacionCastracionSolicitud {

    @NotNull
    private Long animalId;

    private String observaciones;

    public AsignacionCastracionSolicitud() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
