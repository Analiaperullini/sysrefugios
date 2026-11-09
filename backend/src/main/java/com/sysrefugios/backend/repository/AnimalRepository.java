package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByActivoTrue();
    List<Animal> findByOrganizacionIdAndActivoTrue(Long organizacionId);
    Optional<Animal> findByIdAndActivoTrue(Long id);
}
