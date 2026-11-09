package com.sysrefugios.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class EventoHistorialRespuesta {

    private String tipo;

    private LocalDate fecha;

    private Long registroId;

    private boolean activo;

    private String organizacionNombre;

    private String descripcion;

    private List<String> detalles;

    public EventoHistorialRespuesta() {
    }

    public EventoHistorialRespuesta(String tipo, LocalDate fecha, Long registroId, boolean activo,
                                   String organizacionNombre, String descripcion, List<String> detalles) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.registroId = registroId;
        this.activo = activo;
        this.organizacionNombre = organizacionNombre;
        this.descripcion = descripcion;
        this.detalles = detalles;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getOrganizacionNombre() {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre) {
        this.organizacionNombre = organizacionNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<String> detalles) {
        this.detalles = detalles;
    }
}
