// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.course-table tbody tr');

    rows.forEach(row => {
        const courseId = row.cells[0].textContent.toLowerCase();
        const courseName = row.cells[1].textContent.toLowerCase();
        const shouldShow = courseId.includes(searchText) || courseName.includes(searchText);
        row.style.display = shouldShow ? '' : 'none';
    });
} 