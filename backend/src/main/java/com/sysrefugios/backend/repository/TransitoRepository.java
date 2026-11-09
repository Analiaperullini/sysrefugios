package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Transito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransitoRepository extends JpaRepository<Transito, Long> {

    List<Transito> findByActivoTrue();
    List<Transito> findByAnimalIdAndActivoTrue(Long animalId);
    Optional<Transito> findByIdAndActivoTrue(Long id);
    List<Transito> findByAnimalId(Long animalId);
    List<Transito> findByAnimalIdAndFechaFinIsNullAndActivoTrue(Long animalId);
    long countByEspacioTransitoIdAndFechaFinIsNullAndActivoTrue(Long espacioTransitoId);
}
