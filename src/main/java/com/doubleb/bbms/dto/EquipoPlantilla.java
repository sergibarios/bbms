package com.doubleb.bbms.dto;

import com.doubleb.bbms.model.Equipo;

import java.util.List;

public class EquipoPlantilla {

    private final Equipo equipo;
    private final List<FilaPosicion> filas;

    public EquipoPlantilla(Equipo equipo, List<FilaPosicion> filas) {
        this.equipo = equipo;
        this.filas = filas;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public List<FilaPosicion> getFilas() {
        return filas;
    }
}
