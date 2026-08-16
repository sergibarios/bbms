package com.doubleb.bbms.dto;

public class EquipoResumen {

    private final Long id;
    private final String nombre;
    private final String abreviatura;

    public EquipoResumen(Long id, String nombre, String abreviatura) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }
}
