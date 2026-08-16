package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    public List<Jugador> getJugadoresWatchlist(Competicion competicion) {
        return competicion.getEquipos().stream()
                .flatMap(equipo -> equipo.getPlantilla().stream())
                .filter(Jugador::isWatchlisted)
                .filter(jugador -> jugador.getVideos() != null && !jugador.getVideos().isEmpty())
                .toList();
    }
}