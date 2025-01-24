// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.faculty-table tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchText) ? '' : 'none';
    });
}

// 显示教师详情
function showDetails(facultyId) {
    // 跳转到详情页面
    window.location.href = `${contextPath}/dept/faculty/detail?facultyId=${facultyId}`;
}

// 处理离职
function handleLeave(facultyId) {
    if (!confirm('确定要将该教师设置为离职状态吗？')) {
        return;
    }

    fetch(`${contextPath}/dept/faculty?action=leave&facultyId=${facultyId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('操作成功');
                window.location.reload();
            } else {
                alert(data.message || '操作失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('操作失败');
        });
} 