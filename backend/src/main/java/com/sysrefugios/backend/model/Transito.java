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
@Table(name = "transito")
public class Transito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transito")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_espacio", nullable = false)
    private EspacioTransito espacioTransito;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 30)
    private EstadoTransito estado = EstadoTransito.EN_CURSO;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo = true;

    public Transito() {
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

    public EspacioTransito getEspacioTransito() {
        return espacioTransito;
    }

    public void setEspacioTransito(EspacioTransito espacioTransito) {
        this.espacioTransito = espacioTransito;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoTransito getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransito estado) {
        this.estado = estado;
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
