package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.repository.CompeticionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompeticionService {

    private final CompeticionRepository competicionRepository;

    public CompeticionService(CompeticionRepository competicionRepository) {
        this.competicionRepository = competicionRepository;
    }

    public List<Competicion> findAll() {
        return competicionRepository.findAll();
    }

    public Optional<Competicion> findById(Long id) {
        return competicionRepository.findById(id);
    }

    public Competicion save(Competicion competicion) {
        return competicionRepository.save(competicion);
    }

    public void deleteById(Long id) {
        competicionRepository.deleteById(id);
    }
}