package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class IngresoSolicitud {

    @NotBlank
    private String email;

    @NotBlank
    private String contrasena;

    public IngresoSolicitud() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
