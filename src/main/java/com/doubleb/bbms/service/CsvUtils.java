package com.doubleb.bbms.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class CsvUtils {

    private CsvUtils() {}

    static Charset detectarCharset(byte[] bytes) {
        String comoUtf8 = new String(bytes, StandardCharsets.UTF_8);
        if (comoUtf8.indexOf('�') >= 0) {
            return Charset.forName("windows-1252");
        }
        return StandardCharsets.UTF_8;
    }

    static String valor(String raw) {
        if (raw == null) {
            return null;
        }
        String limpio = raw.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    static Integer parseEntero(String raw) {
        String limpio = valor(raw);
        return limpio == null ? null : Integer.parseInt(limpio);
    }
}
