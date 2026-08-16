function nombreCorto(equipo) {
    return (equipo && equipo.abreviatura) ? equipo.abreviatura : (equipo ? equipo.nombre : '?');
}

function mostrarTabComparar(boton) {
    var contenedor = boton.closest('.tabs-comparar-wrap');
    contenedor.querySelectorAll('.tab-btn').forEach(function (b) {
        b.classList.remove('tab-btn--activo');
    });
    contenedor.querySelectorAll('.tab-panel').forEach(function (panel) {
        panel.style.display = 'none';
    });
    boton.classList.add('tab-btn--activo');
    contenedor.querySelector('#' + boton.getAttribute('data-tab')).style.display = 'block';
}

function celdaComparar(equipoId, jornada, dificultades, seleccionados) {
    var partido = PARTIDOS.find(function (p) {
        return p.jornada === jornada
            && (String(p.localId) === equipoId || String(p.visitanteId) === equipoId);
    });

    if (!partido) {
        return '<td>-</td>';
    }

    var esLocal = String(partido.localId) === equipoId;
    var rivalId = String(esLocal ? partido.visitanteId : partido.localId);
    var rival = EQUIPOS.find(function (e) { return String(e.id) === rivalId; });
    var rolRival = esLocal ? 'visitante' : 'local';
    var dificultad = dificultades[rivalId] ? dificultades[rivalId][rolRival] : null;
    var esInterno = seleccionados.indexOf(rivalId) !== -1;

    var badge = dificultad
        ? '<span class="dificultad-badge dificultad-' + dificultad + '">' + dificultad + '</span>'
        : '?';

    return '<td class="comparar-celda' + (esInterno ? ' comparar-celda--interno' : '') + '">'
        + (esLocal ? '🏠 ' : '@ ') + nombreCorto(rival) + ' ' + badge + '</td>';
}

function generarComparativa() {
    var seleccionados = Array.from(document.querySelectorAll('.chk-comparar:checked')).map(function (el) {
        return String(el.value);
    });

    if (seleccionados.length === 0) {
        alert('Selecciona al menos un equipo para comparar.');
        return;
    }

    var dificultades = {};
    EQUIPOS.forEach(function (equipo) {
        var id = String(equipo.id);
        dificultades[id] = {
            local: document.getElementById('dif-local-' + equipo.id).value,
            visitante: document.getElementById('dif-visitante-' + equipo.id).value
        };
    });

    var jornadas = Array.from(new Set(PARTIDOS.map(function (p) { return p.jornada; }).filter(function (j) { return j != null; })));
    jornadas.sort(function (a, b) { return a - b; });

    var mitad = Math.ceil(jornadas.length / 2);
    var grupos = [
        { nombre: 'Primera vuelta', jornadas: jornadas.slice(0, mitad) },
        { nombre: 'Segunda vuelta', jornadas: jornadas.slice(mitad) }
    ].filter(function (grupo) { return grupo.jornadas.length > 0; });

    var idPrefijo = 'tab-comparar-';
    var html = '<div class="tabs-comparar-wrap">';

    if (grupos.length > 1) {
        html += '<div class="tabs-comparar">';
        grupos.forEach(function (grupo, indice) {
            html += '<button type="button" class="btn btn--small tab-btn' + (indice === 0 ? ' tab-btn--activo' : '')
                + '" data-tab="' + idPrefijo + indice + '" onclick="mostrarTabComparar(this)">' + grupo.nombre + '</button>';
        });
        html += '</div>';
    }

    grupos.forEach(function (grupo, indice) {
        html += '<div class="tab-panel" id="' + idPrefijo + indice + '" style="display:' + (indice === 0 ? 'block' : 'none') + ';">';
        html += '<table class="plantilla-table comparar-tabla"><thead><tr><th>Equipo</th>';
        grupo.jornadas.forEach(function (jornada) {
            html += '<th>J' + jornada + '</th>';
        });
        html += '</tr></thead><tbody>';

        seleccionados.forEach(function (equipoId) {
            var equipo = EQUIPOS.find(function (e) { return String(e.id) === equipoId; });
            html += '<tr><td>' + nombreCorto(equipo) + '</td>';
            grupo.jornadas.forEach(function (jornada) {
                html += celdaComparar(equipoId, jornada, dificultades, seleccionados);
            });
            html += '</tr>';
        });

        html += '</tbody></table></div>';
    });

    html += '</div>';
    document.getElementById('resultado-comparativa').innerHTML = html;
}
