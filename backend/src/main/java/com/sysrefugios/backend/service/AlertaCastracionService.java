package com.sysrefugios.backend.service;

import com.sysrefugios.backend.model.Adopcion;
import com.sysrefugios.backend.repository.AdopcionRepository;
import com.sysrefugios.backend.repository.CastracionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AlertaCastracionService {

    private static final Logger REGISTRO = LoggerFactory.getLogger(AlertaCastracionService.class);

    private final AdopcionRepository adopcionRepository;
    private final CastracionRepository castracionRepository;

    public AlertaCastracionService(AdopcionRepository adopcionRepository, CastracionRepository castracionRepository) {
        this.adopcionRepository = adopcionRepository;
        this.castracionRepository = castracionRepository;
    }

    @Scheduled(cron = "${app.alertas-castracion.cron}")
    @EventListener(ApplicationReadyEvent.class)
    public void actualizarTodas() {
        long vencidas = adopcionRepository.findByActivoTrue().stream()
                .filter(this::actualizar)
                .count();
        REGISTRO.info("Revisión diaria de castraciones: {} adopciones con el plazo vencido sin castración registrada", vencidas);
    }

    void actualizarPorAnimal(Long animalId) {
        adopcionRepository.findByAnimalIdAndActivoTrue(animalId).forEach(this::actualizar);
    }

    boolean actualizar(Adopcion adopcion) {
        boolean vencida = LocalDate.now().isAfter(adopcion.getFechaLimiteCastracion())
                && !castracionRepository.existsByAnimalIdAndActivoTrue(adopcion.getAnimal().getId());
        if (vencida != adopcion.isAlertaCastracionVencida()) {
            adopcion.setAlertaCastracionVencida(vencida);
            adopcionRepository.save(adopcion);
        }
        return vencida;
    }
}
