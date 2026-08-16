package com.doubleb.bbms.model.enums;

public enum Europa {
    EUROLEAGUE("Euroliga"),
    EUROCUP("Eurocup"),
    BCL("BCL"),
    EUROPECUP("FIBA Europe Cup"),
    NO("No");

    private final String label;

    Europa(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}