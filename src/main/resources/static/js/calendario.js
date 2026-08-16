(function () {
    var filtroEquipo = document.getElementById('filtro-equipo');
    var filtroJornada = document.getElementById('filtro-jornada');
    var filas = document.querySelectorAll('#tabla-calendario tbody tr');

    var jornadas = [];
    filas.forEach(function (fila) {
        var jornada = fila.getAttribute('data-jornada');
        if (jornada && jornadas.indexOf(jornada) === -1) {
            jornadas.push(jornada);
        }
    });
    jornadas.sort(function (a, b) { return parseInt(a, 10) - parseInt(b, 10); });
    jornadas.forEach(function (jornada) {
        var option = document.createElement('option');
        option.value = jornada;
        option.textContent = 'Jornada ' + jornada;
        filtroJornada.appendChild(option);
    });

    function aplicarFiltrosCalendario() {
        var equipoId = filtroEquipo.value;
        var jornada = filtroJornada.value;

        filas.forEach(function (fila) {
            var coincideEquipo = !equipoId
                || fila.getAttribute('data-local-id') === equipoId
                || fila.getAttribute('data-visitante-id') === equipoId;
            var coincideJornada = !jornada || fila.getAttribute('data-jornada') === jornada;
            fila.style.display = (coincideEquipo && coincideJornada) ? '' : 'none';
        });
    }

    filtroEquipo.addEventListener('change', aplicarFiltrosCalendario);
    filtroJornada.addEventListener('change', aplicarFiltrosCalendario);
})();
