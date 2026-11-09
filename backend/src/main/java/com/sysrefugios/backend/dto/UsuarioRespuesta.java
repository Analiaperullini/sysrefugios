package com.sysrefugios.backend.dto;

import com.sysrefugios.backend.model.EstadoUsuario;
import com.sysrefugios.backend.model.Usuario;

public class UsuarioRespuesta {

    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private Long organizacionId;

    private String organizacionNombre;

    private boolean cambioObligatorio;

    private EstadoUsuario estado;

    public UsuarioRespuesta() {
    }

    public UsuarioRespuesta(Long id, String nombre, String apellido, String email, Long organizacionId, String organizacionNombre, boolean cambioObligatorio, EstadoUsuario estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.cambioObligatorio = cambioObligatorio;
        this.estado = estado;
    }

    public static UsuarioRespuesta desde(Usuario usuario) {
        return new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getOrganizacion().getId(),
                usuario.getOrganizacion().getNombre(),
                usuario.isCambioObligatorio(),
                usuario.getEstado()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }

    public String getOrganizacionNombre() {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre) {
        this.organizacionNombre = organizacionNombre;
    }

    public boolean isCambioObligatorio() {
        return cambioObligatorio;
    }

    public void setCambioObligatorio(boolean cambioObligatorio) {
        this.cambioObligatorio = cambioObligatorio;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
}
