package com.doubleb.bbms.model;

import com.doubleb.bbms.model.enums.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)")
    private Pos posicion;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)")
    private Licencia licencia;

    private String pais;
    private LocalDate dob;
    private Integer cm;
    private Integer contrato;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)")
    private Origen origen;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
    private List<Video> videos;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private boolean watchlisted = false;


    public Jugador(){}

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

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Pos getPosicion() {
        return posicion;
    }

    public void setPosicion(Pos posicion) {
        this.posicion = posicion;
    }

    public Licencia getLicencia() {
        return licencia;
    }

    public void setLicencia(Licencia licencia) {
        this.licencia = licencia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getCm() {
        return cm;
    }

    public void setCm(Integer cm) {
        this.cm = cm;
    }

    public Integer getContrato() {
        return contrato;
    }

    public void setContrato(Integer contrato) {
        this.contrato = contrato;
    }

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Transient
    public Integer getEdad() {
        return dob != null ? Period.between(dob, LocalDate.now()).getYears() : null;
    }

    public boolean isWatchlisted() {
        return watchlisted;
    }
    public void setWatchlisted(boolean watchlisted) {
        this.watchlisted = watchlisted;
    }
}
