package com.doubleb.bbms.controller;

import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import com.doubleb.bbms.service.JugadorService;
import com.doubleb.bbms.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private final JugadorService jugadorService;

    public VideoController(VideoService videoService, JugadorService jugadorService) {
        this.videoService = videoService;
        this.jugadorService = jugadorService;
    }

    @PostMapping
    public String guardar(@ModelAttribute Video video, @RequestParam Long jugadorId) {
        Jugador jugador = jugadorService.findById(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado: " + jugadorId));
        video.setJugador(jugador);
        videoService.save(video);
        return "redirect:/jugadores/" + jugadorId;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam String titulo) {
        Video video = videoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vídeo no encontrado: " + id));
        video.setTitulo(titulo);
        videoService.save(video);
        return "redirect:/jugadores/" + video.getJugador().getId();
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        Video video = videoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vídeo no encontrado: " + id));
        Long jugadorId = video.getJugador().getId();
        videoService.deleteById(id);
        return "redirect:/jugadores/" + jugadorId;
    }
}
