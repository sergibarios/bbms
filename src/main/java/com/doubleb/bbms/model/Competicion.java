package com.doubleb.bbms.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "competiciones")
public class Competicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "temp")
    private Integer year;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "competicion", cascade = CascadeType.ALL)
    private List<Equipo> equipos;

    @OneToOne(mappedBy = "competicion", cascade = CascadeType.ALL)
    private Calendario calendario;

    public Competicion() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }
    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public Calendario getCalendario() {
        return calendario;
    }
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

}
