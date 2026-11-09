package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoAsignacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CambioEstadoAsignacionSolicitud {

    @NotNull
    private EstadoAsignacion estado;

    private String observaciones;

    @Size(max = 150)
    private String veterinario;

    public CambioEstadoAsignacionSolicitud() {
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

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }
}
