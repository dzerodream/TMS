// 月份切换
function changeMonth(month) {
    window.location.href = `${contextPath}/dept/salary/log?month=${month}`;
}

// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.salary-log-table tbody tr');

    rows.forEach(row => {
        const facultyName = row.cells[0].textContent.toLowerCase();
        row.style.display = facultyName.includes(searchText) ? '' : 'none';
    });
} 