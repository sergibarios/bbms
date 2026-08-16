package com.doubleb.bbms.service;

import com.doubleb.bbms.dto.ImportResultado;
import com.doubleb.bbms.model.Calendario;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Partido;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Importa partidos en bloque desde un CSV separado por ";" con columnas:
 * Jornada;Fecha;Hora;Local;Visitante;Pabellón
 * La primera fila se asume cabecera y se ignora. Local/Visitante se resuelven
 * por nombre dentro de la competición dada. Pabellón es opcional: si se deja
 * en blanco se usa el pabellón del equipo local en ese momento. Fecha en
 * formato dd/MM/yyyy, hora en formato HH:mm (opcional).
 */
@Service
public class PartidoImportService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("H:mm");
    private static final int COLUMNAS_ESPERADAS = 6;

    private final EquipoService equipoService;
    private final CalendarioService calendarioService;
    private final PartidoService partidoService;

    public PartidoImportService(EquipoService equipoService, CalendarioService calendarioService, PartidoService partidoService) {
        this.equipoService = equipoService;
        this.calendarioService = calendarioService;
        this.partidoService = partidoService;
    }

    public ImportResultado importar(MultipartFile archivo, Competicion competicion) throws IOException {
        Calendario calendario = calendarioService.findByCompeticion(competicion).orElseGet(() -> {
            Calendario nuevo = new Calendario();
            nuevo.setCompeticion(competicion);
            return calendarioService.save(nuevo);
        });

        Map<String, Equipo> equiposPorNombre = new HashMap<>();
        for (Equipo equipo : equipoService.findByCompeticion(competicion)) {
            equiposPorNombre.put(equipo.getNombre().trim().toLowerCase(), equipo);
        }

        byte[] bytes = archivo.getBytes();
        Charset charset = CsvUtils.detectarCharset(bytes);

        List<String> errores = new ArrayList<>();
        int importados = 0;

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(bytes), charset))) {

            lector.readLine(); // cabecera
            String linea;
            int numeroFila = 1;

            while ((linea = lector.readLine()) != null) {
                numeroFila++;
                if (linea.isBlank()) {
                    continue;
                }

                try {
                    importarFila(linea, calendario, equiposPorNombre);
                    importados++;
                } catch (Exception ex) {
                    errores.add("Fila " + numeroFila + ": " + ex.getMessage());
                }
            }
        }

        return new ImportResultado(importados, errores);
    }

    private void importarFila(String linea, Calendario calendario, Map<String, Equipo> equiposPorNombre) {
        String[] c = linea.split(";", -1);
        if (c.length < COLUMNAS_ESPERADAS) {
            throw new IllegalArgumentException("faltan columnas (se esperaban " + COLUMNAS_ESPERADAS + ", hay " + c.length + ")");
        }

        Equipo local = parseEquipo(c[3], equiposPorNombre);
        Equipo visitante = parseEquipo(c[4], equiposPorNombre);

        Partido partido = new Partido();
        partido.setCalendario(calendario);
        partido.setJornada(CsvUtils.parseEntero(c[0]));
        partido.setFecha(parseFecha(c[1]));
        partido.setHora(parseHora(c[2]));
        partido.setLocal(local);
        partido.setVisitante(visitante);

        String pabellon = CsvUtils.valor(c[5]);
        partido.setPabellon(pabellon != null ? pabellon : local.getPabellon());

        if (partido.getFecha() == null) {
            throw new IllegalArgumentException("falta la fecha");
        }

        partidoService.save(partido);
    }

    private Equipo parseEquipo(String raw, Map<String, Equipo> equiposPorNombre) {
        String limpio = CsvUtils.valor(raw);
        if (limpio == null) {
            throw new IllegalArgumentException("falta el equipo");
        }
        Equipo equipo = equiposPorNombre.get(limpio.toLowerCase());
        if (equipo == null) {
            throw new IllegalArgumentException("equipo desconocido en esta liga: " + limpio);
        }
        return equipo;
    }

    private LocalDate parseFecha(String raw) {
        String limpio = CsvUtils.valor(raw);
        return limpio == null ? null : LocalDate.parse(limpio, FORMATO_FECHA);
    }

    private LocalTime parseHora(String raw) {
        String limpio = CsvUtils.valor(raw);
        return limpio == null ? null : LocalTime.parse(limpio, FORMATO_HORA);
    }
}
