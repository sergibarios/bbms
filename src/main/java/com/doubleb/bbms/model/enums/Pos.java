package com.doubleb.bbms.model.enums;

public enum Pos {
    B("Base"),
    E("Escolta"),
    A("Alero"),
    AP("Ala-pívot"),
    P("Pívot");

    private final String label;

    Pos(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
