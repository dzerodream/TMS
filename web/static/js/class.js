// 显示添加班级模态框
function showAddModal() {
    document.getElementById('addClassModal').style.display = 'block';
}

// 关闭添加班级模态框
function closeAddModal() {
    document.getElementById('addClassModal').style.display = 'none';
    document.getElementById('addClassForm').reset();
}

// 提交添加班级表单
function submitAddClass() {
    const form = document.getElementById('addClassForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateClassForm(data)) {
        return;
    }

    fetch(`${contextPath}/admin/class/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            if (result.success) {
                alert('添加班级成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加班级失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加班级失败，请稍后重试');
        });
}

// 表单验证
function validateClassForm(data) {
    if (!data.classId || data.classId.trim() === '') {
        alert('请输入班级编号');
        return false;
    }

    if (!data.className || data.className.trim() === '') {
        alert('请输入班级名称');
        return false;
    }

    if (!data.departmentId) {
        alert('请选择所属院系');
        return false;
    }

    return true;
}

// 删除班级
function deleteClass(classId) {
    if (!confirm('确定要删除该班级吗？此操作不可恢复。')) {
        return;
    }

    fetch(`${contextPath}/admin/class/delete?classId=${classId}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            if (result.success) {
                alert('删除成功');
                window.location.reload();
            } else {
                alert(result.message || '删除失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('删除失败，请稍后重试');
        });
}

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase();
    const tbody = document.querySelector('.class-table tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let row of rows) {
        const className = row.cells[0].textContent.toLowerCase();
        const departmentName = row.cells[1].textContent.toLowerCase();
        if (className.includes(searchText) || departmentName.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
} 