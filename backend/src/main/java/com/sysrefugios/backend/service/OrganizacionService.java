package com.sysrefugios.backend.service;

import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.exception.RecursoNoEncontradoException;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;
import com.sysrefugios.backend.repository.OrganizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizacionService {

    private final OrganizacionRepository organizacionRepository;

    public OrganizacionService(OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    public List<Organizacion> listar() {
        return organizacionRepository.findByActivoTrue();
    }

    public Organizacion buscarPorId(Long id) {
        return organizacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + id));
    }

    public Organizacion crear(Organizacion organizacion) {
        organizacion.setId(null);
        organizacion.setActivo(true);
        return organizacionRepository.save(organizacion);
    }

    public Organizacion actualizar(Long id, Organizacion datos, Usuario actor) {
        Organizacion existente = buscarPorId(id);
        exigirPropia(existente, actor);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDireccion(datos.getDireccion());
        existente.setEtapaVidaAtendida(datos.getEtapaVidaAtendida());
        return organizacionRepository.save(existente);
    }

    public void eliminar(Long id, Usuario actor) {
        Organizacion existente = buscarPorId(id);
        exigirPropia(existente, actor);
        existente.setActivo(false);
        organizacionRepository.save(existente);
    }

    private void exigirPropia(Organizacion organizacion, Usuario actor) {
        if (!ControlAcceso.esDeLaOrganizacion(organizacion, actor)) {
            throw new AccesoDenegadoException("OTRA_ORGANIZACION",
                    "Solo los usuarios de " + organizacion.getNombre() + " pueden modificar los datos de esa organización");
        }
    }
}
