package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoEspacioTransito;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EspacioTransitoSolicitud {

    @NotNull
    private Long organizacionId;

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @Min(1)
    private Integer capacidad;

    @NotNull
    private EstadoEspacioTransito estado;

    private String observaciones;

    public EspacioTransitoSolicitud() {
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoEspacioTransito getEstado() {
        return estado;
    }

    public void setEstado(EstadoEspacioTransito estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
