package com.doubleb.bbms.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "predicciones_liga")
public class PrediccionLiga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competicion_id")
    private Competicion competicion;

    private LocalDateTime fecha;

    @OneToMany(mappedBy = "prediccionLiga", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("posicion ASC")
    private List<PrediccionPosicion> posiciones;

    public PrediccionLiga() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Competicion getCompeticion() {
        return competicion;
    }
    public void setCompeticion(Competicion competicion) {
        this.competicion = competicion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<PrediccionPosicion> getPosiciones() {
        return posiciones;
    }
    public void setPosiciones(List<PrediccionPosicion> posiciones) {
        this.posiciones = posiciones;
    }
}
