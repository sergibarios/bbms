package com.doubleb.bbms.model.enums;

public enum Origen {
    FICHAJE("Fichaje"),
    RENOVACION("Renovación"),
    CANTERA("Cantera"),
    SIN_EQUIPO("Sin equipo"),
    SIGUE("Sigue"),
    CESION("Cesión"),
    REGRESA("Vuelve de cesión");

    private final String label;

    Origen(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}