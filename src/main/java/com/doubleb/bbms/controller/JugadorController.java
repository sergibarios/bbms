package com.doubleb.bbms.controller;

import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import com.doubleb.bbms.service.EquipoService;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;
    private final EquipoService equipoService;
    private final VideoService videoService;

    public JugadorController(JugadorService jugadorService,
                             EquipoService equipoService,
                             VideoService videoService) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
        this.videoService = videoService;
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Jugador jugador = jugadorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado: " + id));
        model.addAttribute("jugador", jugador);
        model.addAttribute("edad", jugadorService.getEdad(jugador));
        model.addAttribute("videos", videoService.findByJugador(jugador));
        return "jugador";
    }

    @PostMapping
    public String guardar(@ModelAttribute Jugador jugador, @RequestParam Long equipoId,
                          @RequestParam(required = false) String videoUrl) {
        Equipo equipo = equipoService.findById(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + equipoId));
        jugador.setEquipo(equipo);
        jugadorService.save(jugador);

        if (videoUrl != null && !videoUrl.isBlank()) {
            Video video = new Video();
            video.setUrl(videoUrl);
            video.setTitulo("Highlights");
            video.setJugador(jugador);
            videoService.save(video);
        }

        return "redirect:/equipos/" + equipoId;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Jugador jugador,
                             @RequestParam Long equipoId) {
        Equipo equipo = equipoService.findById(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + equipoId));
        jugador.setId(id);
        jugador.setEquipo(equipo);
        jugadorService.save(jugador);
        return "redirect:/jugadores/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        Jugador jugador = jugadorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado: " + id));
        Long equipoId = jugador.getEquipo().getId();
        jugadorService.deleteById(id);
        return "redirect:/equipos/" + equipoId;
    }

    @PostMapping("/{id}/watchlist")
    public String toggleWatchlist(@PathVariable Long id) {
        Jugador jugador = jugadorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado: " + id));
        jugador.setWatchlisted(!jugador.isWatchlisted());
        jugadorService.save(jugador);
        return "redirect:/equipos/" + jugador.getEquipo().getId();
    }
}
