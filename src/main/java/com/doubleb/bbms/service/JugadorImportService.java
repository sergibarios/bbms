package com.doubleb.bbms.service;

import com.doubleb.bbms.dto.ImportResultado;
import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.model.Equipo;
import com.doubleb.bbms.model.Jugador;
import com.doubleb.bbms.model.Video;
import com.doubleb.bbms.model.enums.Licencia;
import com.doubleb.bbms.model.enums.Origen;
import com.doubleb.bbms.model.enums.Pos;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Importa jugadores en bloque desde un CSV separado por ";" con columnas:
 * Equipo;Posición;Nombre;Foto (URL);Licencia;País;Fecha de nacimiento;Altura (cm);
 * Año fin de contrato;Origen;Descripción;Vídeo (URL de YouTube)
 * La primera fila se asume cabecera y se ignora. Equipo se resuelve por nombre
 * dentro de la competición dada. Posición/Licencia aceptan código (B, JFL...)
 * o etiqueta; Origen acepta etiqueta o código.
 */
@Service
public class JugadorImportService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int COLUMNAS_ESPERADAS = 12;

    private final JugadorService jugadorService;
    private final VideoService videoService;
    private final EquipoService equipoService;

    public JugadorImportService(JugadorService jugadorService, VideoService videoService, EquipoService equipoService) {
        this.jugadorService = jugadorService;
        this.videoService = videoService;
        this.equipoService = equipoService;
    }

    public ImportResultado importar(MultipartFile archivo, Competicion competicion) throws IOException {
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
                    importarFila(linea, equiposPorNombre);
                    importados++;
                } catch (Exception ex) {
                    errores.add("Fila " + numeroFila + ": " + ex.getMessage());
                }
            }
        }

        return new ImportResultado(importados, errores);
    }

    private void importarFila(String linea, Map<String, Equipo> equiposPorNombre) {
        String[] c = linea.split(";", -1);
        if (c.length < COLUMNAS_ESPERADAS) {
            throw new IllegalArgumentException("faltan columnas (se esperaban " + COLUMNAS_ESPERADAS + ", hay " + c.length + ")");
        }

        Jugador jugador = new Jugador();
        jugador.setEquipo(parseEquipo(c[0], equiposPorNombre));
        jugador.setPosicion(parsePos(c[1]));
        jugador.setNombre(valor(c[2]));
        jugador.setFotoUrl(valor(c[3]));
        jugador.setLicencia(parseLicencia(c[4]));
        jugador.setPais(valor(c[5]));
        jugador.setDob(parseFecha(c[6]));
        jugador.setCm(parseEntero(c[7]));
        jugador.setContrato(parseEntero(c[8]));
        jugador.setOrigen(parseOrigen(c[9]));
        jugador.setDescripcion(valor(c[10]));

        if (jugador.getNombre() == null) {
            throw new IllegalArgumentException("falta el nombre");
        }

        jugadorService.save(jugador);

        String videoUrl = valor(c[11]);
        if (videoUrl != null) {
            Video video = new Video();
            video.setUrl(videoUrl);
            video.setTitulo("Highlights");
            video.setJugador(jugador);
            videoService.save(video);
        }
    }

    private Equipo parseEquipo(String raw, Map<String, Equipo> equiposPorNombre) {
        String limpio = valor(raw);
        if (limpio == null) {
            throw new IllegalArgumentException("falta el equipo");
        }
        Equipo equipo = equiposPorNombre.get(limpio.toLowerCase());
        if (equipo == null) {
            throw new IllegalArgumentException("equipo desconocido en esta liga: " + limpio);
        }
        return equipo;
    }

    private String valor(String raw) {
        return CsvUtils.valor(raw);
    }

    private Integer parseEntero(String raw) {
        return CsvUtils.parseEntero(raw);
    }

    private LocalDate parseFecha(String raw) {
        String limpio = valor(raw);
        return limpio == null ? null : LocalDate.parse(limpio, FORMATO_FECHA);
    }

    private Pos parsePos(String raw) {
        String limpio = valor(raw);
        if (limpio == null) {
            return null;
        }
        for (Pos p : Pos.values()) {
            if (p.name().equalsIgnoreCase(limpio) || p.getLabel().equalsIgnoreCase(limpio)) {
                return p;
            }
        }
        throw new IllegalArgumentException("posición desconocida: " + limpio);
    }

    private Licencia parseLicencia(String raw) {
        String limpio = valor(raw);
        if (limpio == null) {
            return null;
        }
        for (Licencia l : Licencia.values()) {
            if (l.name().equalsIgnoreCase(limpio) || l.getLabel().equalsIgnoreCase(limpio)) {
                return l;
            }
        }
        throw new IllegalArgumentException("licencia desconocida: " + limpio);
    }

    private Origen parseOrigen(String raw) {
        String limpio = valor(raw);
        if (limpio == null) {
            return null;
        }
        for (Origen o : Origen.values()) {
            if (o.name().equalsIgnoreCase(limpio) || o.getLabel().equalsIgnoreCase(limpio)) {
                return o;
            }
        }
        throw new IllegalArgumentException("origen desconocido: " + limpio);
    }
}
