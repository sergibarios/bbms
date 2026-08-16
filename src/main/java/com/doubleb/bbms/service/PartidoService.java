package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Calendario;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Partido;
import com.doubleb.bbms.repository.PartidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;

    public PartidoService(PartidoRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }

    public List<Partido> findByCalendario(Calendario calendario) {
        return partidoRepository.findByCalendario(calendario);
    }

    public List<Partido> findByEquipo(Equipo equipo) {
        return partidoRepository.findByLocalOrVisitante(equipo, equipo);
    }

    public Optional<Partido> findById(Long id) {
        return partidoRepository.findById(id);
    }

    public Partido save(Partido partido) {
        return partidoRepository.save(partido);
    }

    public void deleteById(Long id) {
        partidoRepository.deleteById(id);
    }
}