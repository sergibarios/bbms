package com.doubleb.bbms.model.enums;

public enum Licencia {
    COT("COT"),
    EUR("EUR"),
    JFL("JFL"),
    EXT("EXT");

    private final String label;

    Licencia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
