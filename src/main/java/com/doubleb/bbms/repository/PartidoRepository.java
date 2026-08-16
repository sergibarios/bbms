package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Calendario;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByCalendario(Calendario calendario);
    List<Partido> findByLocalOrVisitante(Equipo local, Equipo visitante);
}