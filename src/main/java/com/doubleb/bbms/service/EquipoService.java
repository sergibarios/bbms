package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.repository.EquipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    public List<Equipo> findByCompeticion(Competicion competicion) {
        return equipoRepository.findByCompeticion(competicion);
    }

    public Optional<Equipo> findById(Long id) {
        return equipoRepository.findById(id);
    }

    public Equipo save(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public void deleteById(Long id) {
        equipoRepository.deleteById(id);
    }
}