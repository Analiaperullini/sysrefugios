package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {

    List<Adoptante> findByOrganizacionIdAndActivoTrue(Long organizacionId);
    Optional<Adoptante> findByIdAndActivoTrue(Long id);
    Optional<Adoptante> findByDocumentoAndOrganizacionId(String documento, Long organizacionId);
}
