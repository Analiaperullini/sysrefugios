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
@Table(name = "derivacion")
public class Derivacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_derivacion")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_organizacion_origen", nullable = false)
    private Organizacion organizacionOrigen;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_organizacion_destino", nullable = false)
    private Organizacion organizacionDestino;

    @NotNull
    @Column(name = "fecha_derivacion", nullable = false)
    private LocalDate fechaDerivacion;

    @NotBlank
    @Column(nullable = false)
    private String motivo;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo = true;

    public Derivacion() {
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

    public Organizacion getOrganizacionOrigen() {
        return organizacionOrigen;
    }

    public void setOrganizacionOrigen(Organizacion organizacionOrigen) {
        this.organizacionOrigen = organizacionOrigen;
    }

    public Organizacion getOrganizacionDestino() {
        return organizacionDestino;
    }

    public void setOrganizacionDestino(Organizacion organizacionDestino) {
        this.organizacionDestino = organizacionDestino;
    }

    public LocalDate getFechaDerivacion() {
        return fechaDerivacion;
    }

    public void setFechaDerivacion(LocalDate fechaDerivacion) {
        this.fechaDerivacion = fechaDerivacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
