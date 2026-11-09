package com.sysrefugios.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CambioContrasenaSolicitud {

    @NotBlank
    private String contrasenaActual;

    @NotBlank
    private String contrasenaNueva;

    public String getContrasenaActual() {
        return contrasenaActual;
    }

    public void setContrasenaActual(String contrasenaActual) {
        this.contrasenaActual = contrasenaActual;
    }

    public String getContrasenaNueva() {
        return contrasenaNueva;
    }

    public void setContrasenaNueva(String contrasenaNueva) {
        this.contrasenaNueva = contrasenaNueva;
    }
}
