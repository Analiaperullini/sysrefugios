package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.EtapaVida;
import com.sysrefugios.backend.model.Tamanio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AnimalSolicitud {

    @NotBlank
    private String nombre;

    @NotBlank
    private String especie;

    private String sexo;

    private LocalDate fechaNacimiento;

    @Size(max = 50)
    private String color;

    @NotNull
    private EtapaVida etapaVida;

    private Tamanio tamanio;

    @NotNull
    private EstadoAnimal estado;

    private String descripcion;

    @NotNull
    private Long organizacionId;

    public AnimalSolicitud() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EtapaVida getEtapaVida() {
        return etapaVida;
    }

    public void setEtapaVida(EtapaVida etapaVida) {
        this.etapaVida = etapaVida;
    }

    public Tamanio getTamanio() {
        return tamanio;
    }

    public void setTamanio(Tamanio tamanio) {
        this.tamanio = tamanio;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }
}
