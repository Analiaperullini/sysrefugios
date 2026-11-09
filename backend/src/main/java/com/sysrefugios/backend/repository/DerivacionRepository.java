package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Derivacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DerivacionRepository extends JpaRepository<Derivacion, Long> {

    List<Derivacion> findByActivoTrue();
    List<Derivacion> findByAnimalIdAndActivoTrue(Long animalId);
    Optional<Derivacion> findByIdAndActivoTrue(Long id);
    List<Derivacion> findByAnimalId(Long animalId);
}
