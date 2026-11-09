package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.EspacioTransito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspacioTransitoRepository extends JpaRepository<EspacioTransito, Long> {

    List<EspacioTransito> findByActivoTrue();
    List<EspacioTransito> findByOrganizacionIdAndActivoTrue(Long organizacionId);
    Optional<EspacioTransito> findByIdAndActivoTrue(Long id);
}
