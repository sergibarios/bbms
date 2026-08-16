package com.doubleb.bbms.controller;

import com.doubleb.bbms.dto.ImportResultado;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Partido;
import com.doubleb.bbms.service.CompeticionService;
import com.doubleb.bbms.service.EquipoService;
import com.doubleb.bbms.service.JugadorImportService;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.PartidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;
    private final CompeticionService competicionService;
    private final JugadorService jugadorService;
    private final PartidoService partidoService;
    private final JugadorImportService jugadorImportService;

    public EquipoController(EquipoService equipoService,
                            CompeticionService competicionService,
                            JugadorService jugadorService,
                            PartidoService partidoService,
                            JugadorImportService jugadorImportService) {
        this.equipoService = equipoService;
        this.competicionService = competicionService;
        this.jugadorService = jugadorService;
        this.partidoService = partidoService;
        this.jugadorImportService = jugadorImportService;
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Equipo equipo = equipoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + id));
        List<Jugador> jugadores = jugadorService.findByEquipo(equipo);
        jugadores.sort(Comparator.comparing(j -> j.getPosicion() != null ? j.getPosicion().ordinal() : Integer.MAX_VALUE));

        List<Partido> partidos = partidoService.findByEquipo(equipo);
        partidos.sort(Comparator.comparing(Partido::getJornada, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Partido::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Partido::getHora, Comparator.nullsLast(Comparator.naturalOrder())));

        model.addAttribute("equipo", equipo);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("partidos", partidos);
        return "equipo";
    }

    @PostMapping
    public String guardar(@ModelAttribute Equipo equipo, @RequestParam Long competicionId) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));
        equipo.setCompeticion(competicion);
        equipoService.save(equipo);
        return "redirect:/liga/" + competicion.getId();
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Equipo equipo,
                             @RequestParam Long competicionId) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));
        equipo.setId(id);
        equipo.setCompeticion(competicion);
        equipoService.save(equipo);
        return "redirect:/equipos/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        Equipo equipo = equipoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + id));
        Long competicionId = equipo.getCompeticion().getId();
        equipoService.deleteById(id);
        return "redirect:/liga/" + competicionId;
    }

    @PostMapping("/{id}/jugadores/importar")
    public String importarJugadores(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo,
                                    RedirectAttributes redirectAttributes) throws IOException {
        Equipo equipo = equipoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + id));
        ImportResultado resultado = jugadorImportService.importar(archivo, equipo.getCompeticion());
        redirectAttributes.addFlashAttribute("importados", resultado.getImportados());
        redirectAttributes.addFlashAttribute("erroresImport", resultado.getErrores());
        return "redirect:/equipos/" + id;
    }
}