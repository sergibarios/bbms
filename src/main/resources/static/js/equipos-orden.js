(function () {
    var grid = document.getElementById('equipos-grid-orden');
    if (!grid) return;

    var arrastrado = null;

    grid.querySelectorAll('.equipo-card--arrastrable').forEach(function (tarjeta) {
        tarjeta.addEventListener('dragstart', function () {
            arrastrado = tarjeta;
            tarjeta.classList.add('arrastrando');
        });
        tarjeta.addEventListener('dragend', function () {
            tarjeta.classList.remove('arrastrando');
            arrastrado = null;
        });
    });

    function tarjetaMasCercana(x, y) {
        var tarjetas = Array.from(grid.querySelectorAll('.equipo-card--arrastrable:not(.arrastrando)'));
        var mejor = { distancia: Infinity, elemento: null };

        tarjetas.forEach(function (tarjeta) {
            var caja = tarjeta.getBoundingClientRect();
            var centroX = caja.left + caja.width / 2;
            var centroY = caja.top + caja.height / 2;
            var distancia = Math.hypot(x - centroX, y - centroY);
            if (distancia < mejor.distancia) {
                mejor = { distancia: distancia, elemento: tarjeta, despues: x > centroX };
            }
        });

        return mejor;
    }

    grid.addEventListener('dragover', function (e) {
        e.preventDefault();
        if (!arrastrado) return;

        var objetivo = tarjetaMasCercana(e.clientX, e.clientY);
        if (!objetivo.elemento) return;

        if (objetivo.despues) {
            objetivo.elemento.after(arrastrado);
        } else {
            objetivo.elemento.before(arrastrado);
        }
    });
})();
