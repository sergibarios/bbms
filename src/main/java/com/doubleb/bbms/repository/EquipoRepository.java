package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    @Query("SELECT e FROM Equipo e WHERE e.competicion = :competicion ORDER BY e.orden ASC NULLS LAST, e.id ASC")
    List<Equipo> findByCompeticion(@Param("competicion") Competicion competicion);
}