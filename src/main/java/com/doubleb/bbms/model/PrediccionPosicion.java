package com.doubleb.bbms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "predicciones_posicion")
public class PrediccionPosicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prediccion_liga_id")
    private PrediccionLiga prediccionLiga;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    private Integer posicion;

    public PrediccionPosicion() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public PrediccionLiga getPrediccionLiga() {
        return prediccionLiga;
    }
    public void setPrediccionLiga(PrediccionLiga prediccionLiga) {
        this.prediccionLiga = prediccionLiga;
    }

    public Equipo getEquipo() {
        return equipo;
    }
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Integer getPosicion() {
        return posicion;
    }
    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
}
