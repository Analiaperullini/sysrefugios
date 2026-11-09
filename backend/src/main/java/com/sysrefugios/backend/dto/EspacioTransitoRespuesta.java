package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoEspacioTransito;

public class EspacioTransitoRespuesta {

    private Long id;

    private Long organizacionId;

    private String organizacionNombre;

    private String nombre;

    private String descripcion;

    private Integer capacidad;

    private long ocupacion;

    private EstadoEspacioTransito estado;

    private String observaciones;

    public EspacioTransitoRespuesta() {
    }

    public EspacioTransitoRespuesta(Long id, Long organizacionId, String organizacionNombre, String nombre, String descripcion, Integer capacidad, long ocupacion, EstadoEspacioTransito estado, String observaciones) {
        this.id = id;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
        this.ocupacion = ocupacion;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(long ocupacion) {
        this.ocupacion = ocupacion;
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
