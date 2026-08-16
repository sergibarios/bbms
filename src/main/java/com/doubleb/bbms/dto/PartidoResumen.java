package com.doubleb.bbms.dto;

public class PartidoResumen {

    private final Integer jornada;
    private final String fecha;
    private final Long localId;
    private final String localNombre;
    private final Long visitanteId;
    private final String visitanteNombre;

    public PartidoResumen(Integer jornada, String fecha, Long localId, String localNombre,
                          Long visitanteId, String visitanteNombre) {
        this.jornada = jornada;
        this.fecha = fecha;
        this.localId = localId;
        this.localNombre = localNombre;
        this.visitanteId = visitanteId;
        this.visitanteNombre = visitanteNombre;
    }

    public Integer getJornada() {
        return jornada;
    }

    public String getFecha() {
        return fecha;
    }

    public Long getLocalId() {
        return localId;
    }

    public String getLocalNombre() {
        return localNombre;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public String getVisitanteNombre() {
        return visitanteNombre;
    }
}
