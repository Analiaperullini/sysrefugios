package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.CampaniaCastracion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CampaniaCastracionRepository extends JpaRepository<CampaniaCastracion, Long> {

    List<CampaniaCastracion> findByActivoTrueOrderByFechaDesc();
    Optional<CampaniaCastracion> findByIdAndActivoTrue(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CampaniaCastracion c where c.id = :id and c.activo = true")
    Optional<CampaniaCastracion> buscarParaAsignar(@Param("id") Long id);
}
