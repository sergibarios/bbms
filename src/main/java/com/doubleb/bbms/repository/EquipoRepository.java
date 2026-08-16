package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByCompeticion(Competicion competicion);
}