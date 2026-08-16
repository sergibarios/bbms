package com.doubleb.bbms.repository;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.PrediccionLiga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrediccionLigaRepository extends JpaRepository<PrediccionLiga, Long> {
    Optional<PrediccionLiga> findTopByCompeticionOrderByFechaDesc(Competicion competicion);
    List<PrediccionLiga> findByCompeticionOrderByFechaDesc(Competicion competicion);
}
