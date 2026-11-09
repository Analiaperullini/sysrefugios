package com.sysrefugios.backend.service;

import com.sysrefugios.backend.exception.AccesoDenegadoException;
import com.sysrefugios.backend.model.Animal;
import com.sysrefugios.backend.model.Organizacion;
import com.sysrefugios.backend.model.Usuario;

final class ControlAcceso {

    private ControlAcceso() {
    }

    static void exigirAnimalACargo(Animal animal, Usuario actor) {
        Organizacion aCargo = animal.getOrganizacion();
        if (!esDeLaOrganizacion(aCargo, actor)) {
            throw new AccesoDenegadoException("OTRA_ORGANIZACION",
                    animal.getNombre() + " está a cargo de " + aCargo.getNombre()
                            + ": solo esa organización puede registrar o modificar su información. "
                            + actor.getOrganizacion().getNombre() + " puede consultar su historial");
        }
    }

    static void exigirOrganizacionPropia(Organizacion organizacion, Usuario actor, String registro) {
        if (!esDeLaOrganizacion(organizacion, actor)) {
            throw new AccesoDenegadoException("OTRA_ORGANIZACION",
                    registro + " pertenece a " + organizacion.getNombre()
                            + ": solo esa organización puede registrarlo o modificarlo");
        }
    }

    static boolean esDeLaOrganizacion(Organizacion organizacion, Usuario actor) {
        return organizacion.getId().equals(actor.getOrganizacion().getId());
    }
}
