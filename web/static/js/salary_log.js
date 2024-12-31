function changeMonth(month) {
    window.location.href = `${contextPath}/admin/salary/log?month=${month}`;
}

function handleSearch() {
    const input = document.getElementById('searchInput');
    const filter = input.value.toLowerCase();
    const table = document.querySelector('.salary-log-table table');
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) {
        const nameCell = rows[i].getElementsByTagName('td')[0];
        if (nameCell) {
            const name = nameCell.textContent || nameCell.innerText;
            if (name.toLowerCase().indexOf(filter) > -1) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }
} 