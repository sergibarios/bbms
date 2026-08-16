package com.doubleb.bbms.dto;

import com.doubleb.bbms.model.Equipo;

import java.util.List;

public class EquipoWatchlist {

    private final Equipo equipo;
    private final List<JugadorWatchlist> jugadores;

    public EquipoWatchlist(Equipo equipo, List<JugadorWatchlist> jugadores) {
        this.equipo = equipo;
        this.jugadores = jugadores;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public List<JugadorWatchlist> getJugadores() {
        return jugadores;
    }
}
