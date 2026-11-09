package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class AdoptanteRespuesta {

    private Long id;

    private String nombre;

    private String apellido;

    private String documento;

    private String telefono;

    private String email;

    private String direccion;

    private String observaciones;

    private Long organizacionId;

    private String organizacionNombre;

    private boolean consentimientoDatos;

    private LocalDate fechaConsentimiento;

    public AdoptanteRespuesta() {
    }

    public AdoptanteRespuesta(Long id, String nombre, String apellido, String documento, String telefono, String email, String direccion, String observaciones, Long organizacionId, String organizacionNombre, boolean consentimientoDatos, LocalDate fechaConsentimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.observaciones = observaciones;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.consentimientoDatos = consentimientoDatos;
        this.fechaConsentimiento = fechaConsentimiento;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public boolean isConsentimientoDatos() {
        return consentimientoDatos;
    }

    public void setConsentimientoDatos(boolean consentimientoDatos) {
        this.consentimientoDatos = consentimientoDatos;
    }

    public LocalDate getFechaConsentimiento() {
        return fechaConsentimiento;
    }

    public void setFechaConsentimiento(LocalDate fechaConsentimiento) {
        this.fechaConsentimiento = fechaConsentimiento;
    }
}
