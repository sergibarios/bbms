package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    List<Jugador> findByEquipo(Equipo equipo);

}