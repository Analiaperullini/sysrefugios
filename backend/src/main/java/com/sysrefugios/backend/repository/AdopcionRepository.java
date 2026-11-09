package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Adopcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdopcionRepository extends JpaRepository<Adopcion, Long> {

    List<Adopcion> findByActivoTrue();
    List<Adopcion> findByAnimalIdAndActivoTrue(Long animalId);
    Optional<Adopcion> findByIdAndActivoTrue(Long id);
    List<Adopcion> findByAnimalId(Long animalId);
    List<Adopcion> findByOrganizacionIdAndAlertaCastracionVencidaTrueAndActivoTrue(Long organizacionId);
    List<Adopcion> findByAlertaCastracionVencidaTrueAndActivoTrue();
    boolean existsByAnimalIdAndAlertaCastracionVencidaTrueAndActivoTrue(Long animalId);
}
