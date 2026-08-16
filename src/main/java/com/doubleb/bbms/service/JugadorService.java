package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.repository.JugadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    public List<Jugador> findByEquipo(Equipo equipo) {
        return jugadorRepository.findByEquipo(equipo);
    }

    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }

    public Jugador save(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public void deleteById(Long id) {
        jugadorRepository.deleteById(id);
    }

    public int getEdad(Jugador jugador) {
        if (jugador.getDob() == null) {
            return 0;
        }
        return Period.between(jugador.getDob(), LocalDate.now()).getYears();
    }
}