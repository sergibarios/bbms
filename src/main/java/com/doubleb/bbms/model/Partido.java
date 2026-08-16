package com.doubleb.bbms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "partidos")
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calendario_id")
    private Calendario calendario;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Equipo local;

    @ManyToOne
    @JoinColumn(name = "visitante_id")
    private Equipo visitante;

    private Integer jornada;
    private LocalDate fecha;
    private LocalTime hora;
    private String pabellon;

    public Partido() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Calendario getCalendario() {
        return calendario;
    }
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Equipo getLocal() {
        return local;
    }
    public void setLocal(Equipo local) {
        this.local = local;
    }

    public Equipo getVisitante() {
        return visitante;
    }
    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public Integer getJornada() {
        return jornada;
    }
    public void setJornada(Integer jornada) {
        this.jornada = jornada;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getPabellon() {
        return pabellon;
    }
    public void setPabellon(String pabellon) {
        this.pabellon = pabellon;
    }
}