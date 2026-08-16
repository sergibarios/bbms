package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByJugador(Jugador jugador);
    List<Video> findByEquipo(Equipo equipo);
}