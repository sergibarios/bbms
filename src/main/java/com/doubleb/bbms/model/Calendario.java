package com.doubleb.bbms.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "calendarios")
public class Calendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "competicion_id")
    private Competicion competicion;

    @OneToMany(mappedBy = "calendario", cascade = CascadeType.ALL)
    private List<Partido> partidos;

    public Calendario() {}

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

    public List<Partido> getPartidos() {
        return partidos;
    }
    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}