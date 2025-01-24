// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.class-table tbody tr');

    rows.forEach(row => {
        const className = row.cells[0].textContent.toLowerCase();
        row.style.display = className.includes(searchText) ? '' : 'none';
    });
}

// 显示添加模态框
function showAddModal() {
    document.getElementById('addClassModal').style.display = 'block';
    document.getElementById('addClassForm').reset();
}

// 关闭添加模态框
function closeAddModal() {
    document.getElementById('addClassModal').style.display = 'none';
}

// 提交添加班级
function submitAddClass() {
    const form = document.getElementById('addClassForm');
    const formData = {
        classId: form.classId.value,
        className: form.className.value,
        departmentId: form.departmentId.value
    };

    fetch(`${contextPath}/dept/class/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                window.location.reload();
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误，请稍后重试');
        });
}

// 删除班级
function deleteClass(classId) {
    if (!confirm('确定要删除这个班级吗？')) {
        return;
    }

    fetch(`${contextPath}/dept/class/delete?classId=${classId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            if (data.success) {
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误，请稍后重试');
        });
} 