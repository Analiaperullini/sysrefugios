package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoCampania;

import java.time.LocalDate;

public class CampaniaCastracionRespuesta {

    private Long id;

    private Long organizacionId;

    private String organizacionNombre;

    private String nombre;

    private LocalDate fecha;

    private String lugar;

    private Integer cantidadCupos;

    private long cuposUsados;

    private String descripcion;

    private EstadoCampania estado;

    public CampaniaCastracionRespuesta() {
    }

    public CampaniaCastracionRespuesta(Long id, Long organizacionId, String organizacionNombre, String nombre, LocalDate fecha, String lugar, Integer cantidadCupos, long cuposUsados, String descripcion, EstadoCampania estado) {
        this.id = id;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.cantidadCupos = cantidadCupos;
        this.cuposUsados = cuposUsados;
        this.descripcion = descripcion;
        this.estado = estado;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Integer getCantidadCupos() {
        return cantidadCupos;
    }

    public void setCantidadCupos(Integer cantidadCupos) {
        this.cantidadCupos = cantidadCupos;
    }

    public long getCuposUsados() {
        return cuposUsados;
    }

    public void setCuposUsados(long cuposUsados) {
        this.cuposUsados = cuposUsados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoCampania getEstado() {
        return estado;
    }

    public void setEstado(EstadoCampania estado) {
        this.estado = estado;
    }
}
