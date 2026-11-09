package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.EtapaVida;
import com.sysrefugios.backend.model.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {

    List<Organizacion> findByActivoTrue();
    Optional<Organizacion> findByIdAndActivoTrue(Long id);
    Optional<Organizacion> findFirstByEtapaVidaAtendidaAndActivoTrue(EtapaVida etapaVida);
}
