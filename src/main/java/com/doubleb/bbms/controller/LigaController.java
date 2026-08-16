package com.doubleb.bbms.controller;

import com.doubleb.bbms.dto.EquipoPlantilla;
import com.doubleb.bbms.dto.FilaPosicion;
import com.doubleb.bbms.dto.ImportResultado;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Partido;
import com.doubleb.bbms.model.enums.Pos;
import com.doubleb.bbms.service.CalendarioService;
import com.doubleb.bbms.service.CompeticionService;
import com.doubleb.bbms.service.EquipoService;
import com.doubleb.bbms.service.JugadorImportService;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.PartidoImportService;
import com.doubleb.bbms.service.PartidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/liga")
public class LigaController {

    private final CompeticionService competicionService;
    private final EquipoService equipoService;
    private final JugadorService jugadorService;
    private final CalendarioService calendarioService;
    private final PartidoService partidoService;
    private final JugadorImportService jugadorImportService;
    private final PartidoImportService partidoImportService;

    public LigaController(CompeticionService competicionService,
                          EquipoService equipoService,
                          JugadorService jugadorService,
                          CalendarioService calendarioService,
                          PartidoService partidoService,
                          JugadorImportService jugadorImportService,
                          PartidoImportService partidoImportService) {
        this.competicionService = competicionService;
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
        this.calendarioService = calendarioService;
        this.partidoService = partidoService;
        this.jugadorImportService = jugadorImportService;
        this.partidoImportService = partidoImportService;
    }

    @GetMapping("/{competicionId}")
    public String verLiga(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        model.addAttribute("competicion", competicion);
        model.addAttribute("equipos", equipoService.findByCompeticion(competicion));

        return "liga";
    }

    @GetMapping("/{competicionId}/plantillas")
    public String verPlantillas(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        List<EquipoPlantilla> plantillas = new ArrayList<>();
        for (Equipo equipo : equipoService.findByCompeticion(competicion)) {
            List<Jugador> jugadores = jugadorService.findByEquipo(equipo);

            List<FilaPosicion> filas = new ArrayList<>();
            for (Pos posicion : Pos.values()) {
                List<Jugador> deEstaPosicion = jugadores.stream()
                        .filter(j -> j.getPosicion() == posicion)
                        .sorted(Comparator.comparing(Jugador::getNombre))
                        .toList();
                filas.add(new FilaPosicion(posicion, deEstaPosicion));
            }

            plantillas.add(new EquipoPlantilla(equipo, filas));
        }

        model.addAttribute("competicion", competicion);
        model.addAttribute("plantillas", plantillas);
        return "plantillas";
    }

    @PostMapping("/{competicionId}/jugadores/importar")
    public String importarJugadores(@PathVariable Long competicionId, @RequestParam("archivo") MultipartFile archivo,
                                    RedirectAttributes redirectAttributes) throws IOException {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));
        ImportResultado resultado = jugadorImportService.importar(archivo, competicion);
        redirectAttributes.addFlashAttribute("importados", resultado.getImportados());
        redirectAttributes.addFlashAttribute("erroresImport", resultado.getErrores());
        return "redirect:/liga/" + competicionId;
    }

    @GetMapping("/{competicionId}/calendario")
    public String verCalendario(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        model.addAttribute("competicion", competicion);
        model.addAttribute("equipos", equipoService.findByCompeticion(competicion));

        List<Partido> partidos = calendarioService.findByCompeticion(competicion)
                .map(partidoService::findByCalendario)
                .orElseGet(ArrayList::new);
        partidos.sort(Comparator.comparing(Partido::getJornada, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Partido::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Partido::getHora, Comparator.nullsLast(Comparator.naturalOrder())));
        model.addAttribute("partidos", partidos);

        return "calendario";
    }

    @PostMapping("/{competicionId}/calendario/importar")
    public String importarCalendario(@PathVariable Long competicionId, @RequestParam("archivo") MultipartFile archivo,
                                     RedirectAttributes redirectAttributes) throws IOException {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));
        ImportResultado resultado = partidoImportService.importar(archivo, competicion);
        redirectAttributes.addFlashAttribute("importados", resultado.getImportados());
        redirectAttributes.addFlashAttribute("erroresImport", resultado.getErrores());
        return "redirect:/liga/" + competicionId + "/calendario";
    }
}