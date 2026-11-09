package com.sysrefugios.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "adopcion")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adopcion")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_adoptante", nullable = false)
    private Adoptante adoptante;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_organizacion", nullable = false)
    private Organizacion organizacion;

    @NotNull
    @Column(name = "fecha_adopcion", nullable = false)
    private LocalDate fechaAdopcion;

    @Column(nullable = false)
    private boolean cumpleMayorEdad;

    @Column(nullable = false)
    private boolean cumpleResponsabilidad;

    @Column(nullable = false)
    private boolean cumpleEspacioCerrado;

    @Column(nullable = false)
    private boolean compromisoVacunacion;

    @Column(nullable = false)
    private boolean compromisoCastracion;

    @Column(columnDefinition = "TEXT")
    private String condiciones;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @NotNull
    @Column(name = "fecha_limite_castracion", nullable = false)
    private LocalDate fechaLimiteCastracion;

    @Column(nullable = false)
    private boolean alertaCastracionVencida;

    @Column(nullable = false)
    private boolean activo = true;

    public Adopcion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Adoptante getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Adoptante adoptante) {
        this.adoptante = adoptante;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
