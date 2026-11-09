package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Rescate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RescateRepository extends JpaRepository<Rescate, Long> {

    List<Rescate> findByActivoTrue();
    Optional<Rescate> findByIdAndActivoTrue(Long id);
    Optional<Rescate> findFirstByAnimalIdAndActivoTrue(Long animalId);
    List<Rescate> findByAnimalId(Long animalId);
}
