package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    List<Seguimiento> findByActivoTrue();
    List<Seguimiento> findByAdopcionIdAndActivoTrue(Long adopcionId);
    Optional<Seguimiento> findByIdAndActivoTrue(Long id);
    List<Seguimiento> findByAdopcionAnimalId(Long animalId);
}
