package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.PrediccionLiga;
import com.doubleb.bbms.model.PrediccionPosicion;
import com.doubleb.bbms.repository.PrediccionLigaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrediccionService {

    private final PrediccionLigaRepository prediccionLigaRepository;
    private final EquipoService equipoService;

    public PrediccionService(PrediccionLigaRepository prediccionLigaRepository, EquipoService equipoService) {
        this.prediccionLigaRepository = prediccionLigaRepository;
        this.equipoService = equipoService;
    }

    public Optional<PrediccionLiga> actual(Competicion competicion) {
        return prediccionLigaRepository.findTopByCompeticionOrderByFechaDesc(competicion);
    }

    public List<PrediccionLiga> historial(Competicion competicion) {
        return prediccionLigaRepository.findByCompeticionOrderByFechaDesc(competicion);
    }

    public PrediccionLiga crear(Competicion competicion, List<Long> equipoIdsEnOrden) {
        PrediccionLiga prediccion = new PrediccionLiga();
        prediccion.setCompeticion(competicion);
        prediccion.setFecha(LocalDateTime.now());

        List<PrediccionPosicion> posiciones = new ArrayList<>();
        int posicion = 1;
        for (Long equipoId : equipoIdsEnOrden) {
            Equipo equipo = equipoService.findById(equipoId)
                    .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + equipoId));

            PrediccionPosicion fila = new PrediccionPosicion();
            fila.setPrediccionLiga(prediccion);
            fila.setEquipo(equipo);
            fila.setPosicion(posicion);
            posiciones.add(fila);

            equipo.setPrediccion(String.valueOf(posicion));
            equipoService.save(equipo);

            posicion++;
        }
        prediccion.setPosiciones(posiciones);

        return prediccionLigaRepository.save(prediccion);
    }

    public void eliminar(Long id) {
        prediccionLigaRepository.deleteById(id);
    }
}
