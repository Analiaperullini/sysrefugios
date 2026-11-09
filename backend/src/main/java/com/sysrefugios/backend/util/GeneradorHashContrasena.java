package com.sysrefugios.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class GeneradorHashContrasena {

    private static final BCryptPasswordEncoder CODIFICADOR = new BCryptPasswordEncoder();

    private GeneradorHashContrasena() {
    }

    public static String generar(String contrasenaPlana) {
        return CODIFICADOR.encode(contrasenaPlana);
    }

    public static boolean coincide(String contrasenaPlana, String hashGuardado) {
        return CODIFICADOR.matches(contrasenaPlana, hashGuardado);
    }
}
