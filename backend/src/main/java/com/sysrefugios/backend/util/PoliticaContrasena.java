package com.sysrefugios.backend.util;

import java.util.regex.Pattern;

public final class PoliticaContrasena {

    public static final String MENSAJE =
            "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número";

    private static final Pattern PATRON = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    private PoliticaContrasena() {
    }

    public static void validar(String contrasena) {
        if (contrasena == null || !PATRON.matcher(contrasena).matches()) {
            throw new IllegalArgumentException(MENSAJE);
        }
    }
}
