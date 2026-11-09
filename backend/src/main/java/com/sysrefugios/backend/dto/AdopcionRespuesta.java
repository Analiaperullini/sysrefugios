package com.sysrefugios.backend.dto;

import java.time.LocalDate;

public class AdopcionRespuesta {

    private Long id;

    private Long animalId;

    private String animalNombre;

    private Long adoptanteId;

    private String adoptanteNombre;

    private boolean datosAdoptanteReservados;

    private Long organizacionId;

    private String organizacionNombre;

    private LocalDate fechaAdopcion;

    private boolean cumpleMayorEdad;

    private boolean cumpleResponsabilidad;

    private boolean cumpleEspacioCerrado;

    private boolean compromisoVacunacion;

    private boolean compromisoCastracion;

    private String condiciones;

    private String observaciones;

    private LocalDate fechaLimiteCastracion;

    private boolean alertaCastracionVencida;

    public AdopcionRespuesta() {
    }

    public AdopcionRespuesta(Long id, Long animalId, String animalNombre, Long adoptanteId, String adoptanteNombre, boolean datosAdoptanteReservados, Long organizacionId, String organizacionNombre, LocalDate fechaAdopcion, boolean cumpleMayorEdad, boolean cumpleResponsabilidad, boolean cumpleEspacioCerrado, boolean compromisoVacunacion, boolean compromisoCastracion, String condiciones, String observaciones, LocalDate fechaLimiteCastracion, boolean alertaCastracionVencida) {
        this.id = id;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.adoptanteId = adoptanteId;
        this.adoptanteNombre = adoptanteNombre;
        this.datosAdoptanteReservados = datosAdoptanteReservados;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.fechaAdopcion = fechaAdopcion;
        this.cumpleMayorEdad = cumpleMayorEdad;
        this.cumpleResponsabilidad = cumpleResponsabilidad;
        this.cumpleEspacioCerrado = cumpleEspacioCerrado;
        this.compromisoVacunacion = compromisoVacunacion;
        this.compromisoCastracion = compromisoCastracion;
        this.condiciones = condiciones;
        this.observaciones = observaciones;
        this.fechaLimiteCastracion = fechaLimiteCastracion;
        this.alertaCastracionVencida = alertaCastracionVencida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getAnimalNombre() {
        return animalNombre;
    }

    public void setAnimalNombre(String animalNombre) {
        this.animalNombre = animalNombre;
    }

    public Long getAdoptanteId() {
        return adoptanteId;
    }

    public void setAdoptanteId(Long adoptanteId) {
        this.adoptanteId = adoptanteId;
    }

    public String getAdoptanteNombre() {
        return adoptanteNombre;
    }

    public void setAdoptanteNombre(String adoptanteNombre) {
        this.adoptanteNombre = adoptanteNombre;
    }

    public boolean isDatosAdoptanteReservados() {
        return datosAdoptanteReservados;
    }

    public void setDatosAdoptanteReservados(boolean datosAdoptanteReservados) {
        this.datosAdoptanteReservados = datosAdoptanteReservados;
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

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public boolean isCumpleMayorEdad() {
        return cumpleMayorEdad;
    }

    public void setCumpleMayorEdad(boolean cumpleMayorEdad) {
        this.cumpleMayorEdad = cumpleMayorEdad;
    }

    public boolean isCumpleResponsabilidad() {
        return cumpleResponsabilidad;
    }

    public void setCumpleResponsabilidad(boolean cumpleResponsabilidad) {
        this.cumpleResponsabilidad = cumpleResponsabilidad;
    }

    public boolean isCumpleEspacioCerrado() {
        return cumpleEspacioCerrado;
    }

    public void setCumpleEspacioCerrado(boolean cumpleEspacioCerrado) {
        this.cumpleEspacioCerrado = cumpleEspacioCerrado;
    }

    public boolean isCompromisoVacunacion() {
        return compromisoVacunacion;
    }

    public void setCompromisoVacunacion(boolean compromisoVacunacion) {
        this.compromisoVacunacion = compromisoVacunacion;
    }

    public boolean isCompromisoCastracion() {
        return compromisoCastracion;
    }

    public void setCompromisoCastracion(boolean compromisoCastracion) {
        this.compromisoCastracion = compromisoCastracion;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaLimiteCastracion() {
        return fechaLimiteCastracion;
    }

    public void setFechaLimiteCastracion(LocalDate fechaLimiteCastracion) {
        this.fechaLimiteCastracion = fechaLimiteCastracion;
    }

    public boolean isAlertaCastracionVencida() {
        return alertaCastracionVencida;
    }

    public void setAlertaCastracionVencida(boolean alertaCastracionVencida) {
        this.alertaCastracionVencida = alertaCastracionVencida;
    }
}
