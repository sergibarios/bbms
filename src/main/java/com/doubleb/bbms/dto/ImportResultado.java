package com.doubleb.bbms.dto;

import java.util.List;

public class ImportResultado {

    private final int importados;
    private final List<String> errores;

    public ImportResultado(int importados, List<String> errores) {
        this.importados = importados;
        this.errores = errores;
    }

    public int getImportados() {
        return importados;
    }

    public List<String> getErrores() {
        return errores;
    }
}
