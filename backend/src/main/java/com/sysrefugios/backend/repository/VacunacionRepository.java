package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Vacunacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacunacionRepository extends JpaRepository<Vacunacion, Long> {

    List<Vacunacion> findByActivoTrue();
    List<Vacunacion> findByAnimalIdAndActivoTrue(Long animalId);
    Optional<Vacunacion> findByIdAndActivoTrue(Long id);
    List<Vacunacion> findByAnimalId(Long animalId);
}
