package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Castracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CastracionRepository extends JpaRepository<Castracion, Long> {

    List<Castracion> findByActivoTrue();
    List<Castracion> findByAnimalIdAndActivoTrue(Long animalId);
    Optional<Castracion> findByIdAndActivoTrue(Long id);
    List<Castracion> findByAnimalId(Long animalId);
    boolean existsByAnimalIdAndActivoTrue(Long animalId);
}
