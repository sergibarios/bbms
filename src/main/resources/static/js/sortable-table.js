document.addEventListener('click', function (e) {
    var th = e.target.closest('table.sortable th[data-sort]');
    if (!th) return;

    var table = th.closest('table');
    var tbody = table.tBodies[0];
    var index = Array.prototype.indexOf.call(th.parentNode.children, th);
    var asc = th.getAttribute('data-sort-dir') !== 'asc';

    Array.prototype.forEach.call(table.querySelectorAll('th[data-sort]'), function (h) {
        h.removeAttribute('data-sort-dir');
    });
    th.setAttribute('data-sort-dir', asc ? 'asc' : 'desc');

    var rows = Array.prototype.slice.call(tbody.querySelectorAll('tr'));
    rows.sort(function (rowA, rowB) {
        var cellA = rowA.children[index];
        var cellB = rowB.children[index];
        var valA = cellA.getAttribute('data-value') || cellA.textContent.trim();
        var valB = cellB.getAttribute('data-value') || cellB.textContent.trim();
        var numA = parseFloat(valA);
        var numB = parseFloat(valB);
        var cmp;

        if (!isNaN(numA) && !isNaN(numB)) {
            cmp = numA - numB;
        } else {
            cmp = valA.localeCompare(valB, 'es');
        }

        return asc ? cmp : -cmp;
    });

    rows.forEach(function (row) {
        tbody.appendChild(row);
    });
});
