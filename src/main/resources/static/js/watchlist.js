document.addEventListener('click', function (e) {
    var btn = e.target.closest('.watchlist-video');
    if (!btn) return;

    var player = document.getElementById('watchlist-player');
    var vacio = document.getElementById('watchlist-player-vacio');
    player.src = btn.getAttribute('data-video-src');
    vacio.style.display = 'none';

    document.querySelectorAll('.watchlist-video.activo').forEach(function (el) {
        el.classList.remove('activo');
    });
    btn.classList.add('activo');
});

function aplicarFiltrosWatchlist() {
    var equipoId = document.getElementById('filtro-equipo').value;
    var nombreJugador = document.getElementById('filtro-jugador').value.trim().toLowerCase();

    document.querySelectorAll('.watchlist-grupo').forEach(function (grupo) {
        var coincideEquipo = !equipoId || grupo.getAttribute('data-equipo-id') === equipoId;
        grupo.style.display = coincideEquipo ? '' : 'none';
        if (!coincideEquipo) return;

        var algunJugadorVisible = false;

        grupo.querySelectorAll('.watchlist-seccion').forEach(function (seccion) {
            var nombre = (seccion.getAttribute('data-jugador-nombre') || '').toLowerCase();
            var coincide = !nombreJugador || nombre.includes(nombreJugador);
            seccion.style.display = coincide ? '' : 'none';
            if (coincide) algunJugadorVisible = true;
        });

        grupo.style.display = algunJugadorVisible ? '' : 'none';
    });
}

document.getElementById('filtro-equipo').addEventListener('change', aplicarFiltrosWatchlist);
document.getElementById('filtro-jugador').addEventListener('input', aplicarFiltrosWatchlist);
