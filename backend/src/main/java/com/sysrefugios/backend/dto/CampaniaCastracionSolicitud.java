package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoCampania;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CampaniaCastracionSolicitud {

    @NotBlank
    private String nombre;

    @NotNull
    private LocalDate fecha;

    private String lugar;

    @NotNull
    @Min(1)
    private Integer cantidadCupos;

    private String descripcion;

    private EstadoCampania estado;

    public CampaniaCastracionSolicitud() {
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
