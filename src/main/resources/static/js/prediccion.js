(function () {
    function elementoTrasPuntero(lista, y) {
        var elementos = Array.from(lista.querySelectorAll('.prediccion-item:not(.arrastrando)'));
        return elementos.reduce(function (masCercano, elemento) {
            var caja = elemento.getBoundingClientRect();
            var offset = y - caja.top - caja.height / 2;
            if (offset < 0 && offset > masCercano.offset) {
                return { offset: offset, elemento: elemento };
            }
            return masCercano;
        }, { offset: Number.NEGATIVE_INFINITY, elemento: null }).elemento;
    }

    function habilitarDragDrop(lista) {
        var arrastrado = null;

        lista.querySelectorAll('.prediccion-item').forEach(function (item) {
            item.addEventListener('dragstart', function () {
                arrastrado = item;
                item.classList.add('arrastrando');
            });
            item.addEventListener('dragend', function () {
                item.classList.remove('arrastrando');
                arrastrado = null;
            });
        });

        lista.addEventListener('dragover', function (e) {
            e.preventDefault();
            if (!arrastrado) return;
            var despues = elementoTrasPuntero(lista, e.clientY);
            if (despues == null) {
                lista.appendChild(arrastrado);
            } else {
                lista.insertBefore(arrastrado, despues);
            }
        });
    }

    document.querySelectorAll('.prediccion-lista:not(.prediccion-lista--fija)').forEach(habilitarDragDrop);
})();
