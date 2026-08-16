package com.doubleb.bbms.dto;

public class VideoWatchlist {

    private final String titulo;
    private final String url;
    private final String embedUrl;

    public VideoWatchlist(String titulo, String url, String embedUrl) {
        this.titulo = titulo;
        this.url = url;
        this.embedUrl = embedUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }
}
