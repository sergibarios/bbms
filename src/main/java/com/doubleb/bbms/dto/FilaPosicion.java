package com.doubleb.bbms.dto;

import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.enums.Pos;

import java.util.List;

public class FilaPosicion {

    private final Pos posicion;
    private final List<Jugador> jugadores;

    public FilaPosicion(Pos posicion, List<Jugador> jugadores) {
        this.posicion = posicion;
        this.jugadores = jugadores;
    }

    public Pos getPosicion() {
        return posicion;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}
