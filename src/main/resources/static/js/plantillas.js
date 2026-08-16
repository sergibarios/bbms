document.querySelectorAll('.filtro-equipo').forEach(function (checkbox) {
    checkbox.addEventListener('change', aplicarFiltrosPlantillas);
});

function aplicarFiltrosPlantillas() {
    document.querySelectorAll('.filtro-equipo').forEach(function (checkbox) {
        var wrap = document.querySelector('.plantilla-mini-wrap[data-equipo-id="' + checkbox.value + '"]');
        if (wrap) {
            wrap.style.display = checkbox.checked ? '' : 'none';
        }
    });
}
