package com.doubleb.bbms.dto;

import com.doubleb.bbms.model.Equipo;

import java.util.List;

public class EnfrentamientoFila {

    private final Equipo equipoLocal;
    private final List<String> celdas;

    public EnfrentamientoFila(Equipo equipoLocal, List<String> celdas) {
        this.equipoLocal = equipoLocal;
        this.celdas = celdas;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public List<String> getCeldas() {
        return celdas;
    }
}
