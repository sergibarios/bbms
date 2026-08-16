package com.doubleb.bbms.dto;

import com.doubleb.bbms.model.Jugador;

import java.util.List;

public class JugadorWatchlist {

    private final Jugador jugador;
    private final List<VideoWatchlist> videos;

    public JugadorWatchlist(Jugador jugador, List<VideoWatchlist> videos) {
        this.jugador = jugador;
        this.videos = videos;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public List<VideoWatchlist> getVideos() {
        return videos;
    }
}
