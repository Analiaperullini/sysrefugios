package com.sysrefugios.backend.dto;

import java.util.List;

public class HistorialRespuesta {

    private AnimalRespuesta animal;

    private List<EventoHistorialRespuesta> eventos;

    public HistorialRespuesta() {
    }

    public HistorialRespuesta(AnimalRespuesta animal, List<EventoHistorialRespuesta> eventos) {
        this.animal = animal;
        this.eventos = eventos;
    }

    public AnimalRespuesta getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalRespuesta animal) {
        this.animal = animal;
    }

    public List<EventoHistorialRespuesta> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoHistorialRespuesta> eventos) {
        this.eventos = eventos;
    }
}
