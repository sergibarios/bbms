package com.doubleb.bbms.model;

import com.doubleb.bbms.model.enums.Europa;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_oficial", nullable = false)
    private String nombre;

    @Column(name = "abreviatura", length = 3)
    private String abreviatura;

    @Column(name = "logo_url")
    private String logoUrl;

    @ManyToOne
    @JoinColumn(name = "competicion_id")
    private Competicion competicion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String staff;
    private String entrenador;
    private String ubicacion;
    private String pabellon;
    private String prediccion;

    @Column(columnDefinition = "TEXT")
    private String historial;

    private Integer orden;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)")
    private Europa europa;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
    private List<Jugador> plantilla;

    public Equipo() {}

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

    public String getAbreviatura() {
        return abreviatura;
    }
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Competicion getCompeticion() {
        return competicion;
    }
    public void setCompeticion(Competicion competicion) {
        this.competicion = competicion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getStaff() {
        return staff;
    }
    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getEntrenador() {
        return entrenador;
    }
    public void setEntrenador(String entrenador) {
        this.entrenador = entrenador;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getPabellon() {
        return pabellon;
    }
    public void setPabellon(String pabellon) {
        this.pabellon = pabellon;
    }

    public String getPrediccion() {
        return prediccion;
    }
    public void setPrediccion(String prediccion) {
        this.prediccion = prediccion;
    }

    public String getHistorial() {
        return historial;
    }
    public void setHistorial(String historial) {
        this.historial = historial;
    }

    public Europa getEuropa() {
        return europa;
    }

    public void setEuropa(Europa europa) {
        this.europa = europa;
    }

    public Integer getOrden() {
        return orden;
    }
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<Jugador> getPlantilla() {
        return plantilla;
    }
    public void setPlantilla(List<Jugador> plantilla) {
        this.plantilla = plantilla;
    }
}