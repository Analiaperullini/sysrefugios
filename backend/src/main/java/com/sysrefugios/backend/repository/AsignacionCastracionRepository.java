package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.AsignacionCastracion;
import com.sysrefugios.backend.model.EstadoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionCastracionRepository extends JpaRepository<AsignacionCastracion, Long> {

    List<AsignacionCastracion> findByCampaniaIdOrderByFechaAsignacionAscIdAsc(Long campaniaId);
    Optional<AsignacionCastracion> findByCampaniaIdAndAnimalId(Long campaniaId, Long animalId);
    long countByCampaniaIdAndEstadoNot(Long campaniaId, EstadoAsignacion estado);
    List<AsignacionCastracion> findByAnimalId(Long animalId);
}
