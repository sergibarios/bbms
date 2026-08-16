package com.doubleb.bbms.controller;

import com.doubleb.bbms.dto.EquipoWatchlist;
import com.doubleb.bbms.dto.JugadorWatchlist;
import com.doubleb.bbms.dto.VideoWatchlist;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import com.doubleb.bbms.service.CompeticionService;
import com.doubleb.bbms.service.EquipoService;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/liga")
public class WatchlistController {

    private final CompeticionService competicionService;
    private final EquipoService equipoService;
    private final JugadorService jugadorService;
    private final VideoService videoService;

    public WatchlistController(CompeticionService competicionService,
                               EquipoService equipoService,
                               JugadorService jugadorService,
                               VideoService videoService) {
        this.competicionService = competicionService;
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
        this.videoService = videoService;
    }

    @GetMapping("/{competicionId}/watchlist")
    public String verWatchlist(@PathVariable Long competicionId, Model model) {
        Competicion competicion = competicionService.findById(competicionId)
                .orElseThrow(() -> new IllegalArgumentException("Competición no encontrada: " + competicionId));

        List<EquipoWatchlist> grupos = new ArrayList<>();

        for (Equipo equipo : equipoService.findByCompeticion(competicion)) {
            List<Jugador> jugadoresWatchlisted = jugadorService.findByEquipo(equipo).stream()
                    .filter(Jugador::isWatchlisted)
                    .toList();

            if (jugadoresWatchlisted.isEmpty()) {
                continue;
            }

            List<JugadorWatchlist> jugadores = jugadoresWatchlisted.stream()
                    .map(jugador -> new JugadorWatchlist(jugador, aVideoWatchlist(videoService.findByJugador(jugador))))
                    .toList();

            grupos.add(new EquipoWatchlist(equipo, jugadores));
        }

        model.addAttribute("competicion", competicion);
        model.addAttribute("grupos", grupos);
        return "watchlist";
    }

    private List<VideoWatchlist> aVideoWatchlist(List<Video> videos) {
        List<VideoWatchlist> resultado = new ArrayList<>();
        for (Video video : videos) {
            try {
                resultado.add(new VideoWatchlist(video.getTitulo(), video.getUrl(), videoService.getEmbedUrl(video)));
            } catch (IllegalArgumentException ignorado) {
                // URL no reconocida como vídeo de YouTube: se omite de la watchlist en vez de romper la página
            }
        }
        return resultado;
    }
}
