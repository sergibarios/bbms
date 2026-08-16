package com.doubleb.bbms.controller;

import com.doubleb.bbms.dto.EnfrentamientoFila;
import com.doubleb.bbms.dto.EquipoPlantilla;
import com.doubleb.bbms.dto.EquipoResumen;
import com.doubleb.bbms.dto.FilaPosicion;
import com.doubleb.bbms.dto.ImportResultado;
import com.doubleb.bbms.dto.PartidoResumen;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Partido;
import com.doubleb.bbms.model.PrediccionLiga;
import com.doubleb.bbms.model.enums.Pos;
import com.doubleb.bbms.service.CalendarioService;
import com.doubleb.bbms.service.CompeticionService;
import com.doubleb.bbms.service.EquipoService;
import com.doubleb.bbms.service.JugadorImportService;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.PartidoImportService;
import com.doubleb.bbms.service.PartidoService;
import com.doubleb.bbms.service.PrediccionService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final PrediccionService prediccionService;

    public LigaController(CompeticionService competicionService,
                          EquipoService equipoService,
                          JugadorService jugadorService,
                          CalendarioService calendarioService,
                          PartidoService partidoService,
                          JugadorImportService jugadorImportService,
                          PartidoImportService partidoImportService,
                          PrediccionService prediccionService) {
        this.competicionService = competicionService;
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
        this.calendarioService = calendarioService;
        this.partidoService = partidoService;
        this.jugadorImportService = jugadorImportService;
        this.partidoImportService = partidoImportService;
        this.prediccionService = prediccionService;
    }

    @GetMapping("/{competicionId}")
    public String verLiga(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        model.addAttribute("competicion", competicion);
        model.addAttribute("equipos", equipoService.findByCompeticion(competicion));
        model.addAttribute("prediccionActual", prediccionService.actual(competicion).orElse(null));

        return "liga";
    }

    @PostMapping("/{competicionId}/prediccion")
    public String guardarPrediccion(@PathVariable Long competicionId, @RequestParam("equipoIds") List<Long> equipoIds) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));
        prediccionService.crear(competicion, equipoIds);
        return "redirect:/liga/" + competicionId;
    }

    @PostMapping("/{competicionId}/equipos/orden")
    public String guardarOrdenEquipos(@PathVariable Long competicionId, @RequestParam("equipoIds") List<Long> equipoIds) {
        int orden = 0;
        for (Long equipoId : equipoIds) {
            Equipo equipo = equipoService.findById(equipoId)
                    .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + equipoId));
            equipo.setOrden(orden);
            equipoService.save(equipo);
            orden++;
        }
        return "redirect:/liga/" + competicionId;
    }

    @GetMapping("/{competicionId}/prediccion/historial")
    public String verHistorialPrediccion(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        List<PrediccionLiga> historial = prediccionService.historial(competicion);

        model.addAttribute("competicion", competicion);
        model.addAttribute("historial", historial);
        return "prediccion-historial";
    }

    @PostMapping("/{competicionId}/prediccion/{prediccionId}/eliminar")
    public String eliminarPrediccion(@PathVariable Long competicionId, @PathVariable Long prediccionId) {
        prediccionService.eliminar(prediccionId);
        return "redirect:/liga/" + competicionId + "/prediccion/historial";
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

    @GetMapping("/{competicionId}/enfrentamientos")
    public String verEnfrentamientos(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        List<Equipo> equipos = equipoService.findByCompeticion(competicion);
        List<Partido> partidos = calendarioService.findByCompeticion(competicion)
                .map(partidoService::findByCalendario)
                .orElseGet(ArrayList::new);

        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM");
        Map<String, String> fechaPorPareja = new HashMap<>();
        for (Partido partido : partidos) {
            if (partido.getFecha() == null) {
                continue;
            }
            String clave = partido.getLocal().getId() + "_" + partido.getVisitante().getId();
            String fecha = formatoCorto.format(partido.getFecha());
            fechaPorPareja.merge(clave, fecha, (actual, nueva) -> actual);
        }

        List<EnfrentamientoFila> filas = new ArrayList<>();
        for (Equipo local : equipos) {
            List<String> celdas = new ArrayList<>();
            for (Equipo visitante : equipos) {
                if (local.getId().equals(visitante.getId())) {
                    celdas.add(null);
                } else {
                    celdas.add(fechaPorPareja.get(local.getId() + "_" + visitante.getId()));
                }
            }
            filas.add(new EnfrentamientoFila(local, celdas));
        }

        model.addAttribute("competicion", competicion);
        model.addAttribute("equipos", equipos);
        model.addAttribute("filas", filas);
        return "enfrentamientos";
    }

    @GetMapping("/{competicionId}/comparar-calendarios")
    public String verCompararCalendarios(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        List<Equipo> equipos = equipoService.findByCompeticion(competicion);
        List<Partido> partidos = calendarioService.findByCompeticion(competicion)
                .map(partidoService::findByCalendario)
                .orElseGet(ArrayList::new);

        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM");
        List<EquipoResumen> equiposResumen = equipos.stream()
                .map(e -> new EquipoResumen(e.getId(), e.getNombre(), e.getAbreviatura()))
                .toList();
        List<PartidoResumen> partidosResumen = partidos.stream()
                .map(p -> new PartidoResumen(
                        p.getJornada(),
                        p.getFecha() != null ? formatoCorto.format(p.getFecha()) : null,
                        p.getLocal().getId(), p.getLocal().getNombre(),
                        p.getVisitante().getId(), p.getVisitante().getNombre()))
                .toList();

        model.addAttribute("competicion", competicion);
        model.addAttribute("equipos", equipos);
        model.addAttribute("equiposResumen", equiposResumen);
        model.addAttribute("partidosResumen", partidosResumen);
        return "comparar-calendarios";
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