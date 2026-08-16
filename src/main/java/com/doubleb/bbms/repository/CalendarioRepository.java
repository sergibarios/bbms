package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Calendario;
import com.doubleb.bbms.model.Competicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarioRepository extends JpaRepository<Calendario, Long> {
    Optional<Calendario> findByCompeticion(Competicion competicion);

}