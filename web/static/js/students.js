function changeCourse(courseId) {
    if (!courseId) return;
    window.location.href = `${contextPath}/teacher/students?courseId=${courseId}`;
}

function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const tbody = document.querySelector('.students-table tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let row of rows) {
        const studentId = row.cells[0].textContent.toLowerCase();
        const studentName = row.cells[1].textContent.toLowerCase();
        const className = row.cells[2].textContent.toLowerCase();

        if (studentId.includes(searchText) ||
            studentName.includes(searchText) ||
            className.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
}

// 页面加载完成后的初始化
document.addEventListener('DOMContentLoaded', function () {
    console.log('Students page loaded');
}); 