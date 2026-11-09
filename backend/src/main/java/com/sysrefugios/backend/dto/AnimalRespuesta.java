package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoAnimal;
import com.sysrefugios.backend.model.EtapaVida;
import com.sysrefugios.backend.model.Tamanio;

import java.time.LocalDate;

public class AnimalRespuesta {

    private Long id;

    private String nombre;

    private String especie;

    private String sexo;

    private LocalDate fechaNacimiento;

    private String color;

    private EtapaVida etapaVida;

    private Tamanio tamanio;

    private EstadoAnimal estado;

    private String descripcion;

    private Long organizacionId;

    private String organizacionNombre;

    private String avisoDerivacion;

    private boolean alertaCastracionVencida;

    public AnimalRespuesta() {
    }

    public AnimalRespuesta(Long id, String nombre, String especie, String sexo, LocalDate fechaNacimiento, String color, EtapaVida etapaVida, Tamanio tamanio, EstadoAnimal estado, String descripcion, Long organizacionId, String organizacionNombre, String avisoDerivacion) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.color = color;
        this.etapaVida = etapaVida;
        this.tamanio = tamanio;
        this.estado = estado;
        this.descripcion = descripcion;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.avisoDerivacion = avisoDerivacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrganizacionNombre() {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre) {
        this.organizacionNombre = organizacionNombre;
    }

    public String getAvisoDerivacion() {
        return avisoDerivacion;
    }

    public void setAvisoDerivacion(String avisoDerivacion) {
        this.avisoDerivacion = avisoDerivacion;
    }

    public boolean isAlertaCastracionVencida() {
        return alertaCastracionVencida;
    }

    public void setAlertaCastracionVencida(boolean alertaCastracionVencida) {
        this.alertaCastracionVencida = alertaCastracionVencida;
    }
}
