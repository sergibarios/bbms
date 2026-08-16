package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Calendario;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.repository.CalendarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;

    public CalendarioService(CalendarioRepository calendarioRepository) {
        this.calendarioRepository = calendarioRepository;
    }

    public Optional<Calendario> findByCompeticion(Competicion competicion) {
        return calendarioRepository.findByCompeticion(competicion);
    }

    public Optional<Calendario> findById(Long id) {
        return calendarioRepository.findById(id);
    }

    public Calendario save(Calendario calendario) {
        return calendarioRepository.save(calendario);
    }

    public void deleteById(Long id) {
        calendarioRepository.deleteById(id);
    }
}