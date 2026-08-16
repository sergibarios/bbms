package com.doubleb.bbms.service;

import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import com.doubleb.bbms.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> findByJugador(Jugador jugador) {
        return videoRepository.findByJugador(jugador);
    }

    public List<Video> findByEquipo(Equipo equipo) {
        return videoRepository.findByEquipo(equipo);
    }

    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    public Video save(Video video) {
        return videoRepository.save(video);
    }

    public void deleteById(Long id) {
        videoRepository.deleteById(id);
    }

    public String getEmbedUrl(Video video) {
        String url = video.getUrl();
        String videoId;

        if (url.contains("youtu.be/")) {
            videoId = url.substring(url.indexOf("youtu.be/") + 9).split("[?&]")[0];
        } else if (url.contains("watch?v=")) {
            videoId = url.substring(url.indexOf("watch?v=") + 8).split("[?&]")[0];
        } else {
            throw new IllegalArgumentException("URL de YouTube no reconocida: " + url);
        }

        return "https://www.youtube.com/embed/" + videoId;
    }

}