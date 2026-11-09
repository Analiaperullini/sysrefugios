package com.sysrefugios.backend.repository;

import com.sysrefugios.backend.model.EstadoUsuario;
import com.sysrefugios.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByOrganizacionIdAndEstadoNot(Long organizacionId, EstadoUsuario estado);
}
