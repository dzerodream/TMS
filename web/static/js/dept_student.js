// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.student-table tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchText) ? '' : 'none';
    });
}

// 显示学生详情
function showDetails(studentId) {
    window.location.href = `${contextPath}/dept/student/detail?studentId=${studentId}`;
}

// 设置毕业状态
function handleGraduate(studentId) {
    if (!confirm('确定要将该学生设置为毕业状态吗？')) {
        return;
    }

    fetch(`${contextPath}/dept/student?action=graduate&studentId=${studentId}`, {
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

// 删除学生
function handleDelete(studentId) {
    if (!confirm('确定要删除该学生吗？此操作不可恢复！')) {
        return;
    }

    fetch(`${contextPath}/dept/student?action=delete&studentId=${studentId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('删除成功');
                window.location.reload();
            } else {
                alert(data.message || '删除失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('删除失败');
        });
}

// 显示添加模态框
function showAddModal() {
    const modal = document.getElementById('addModal');
    modal.style.display = 'block';
    // 设置默认入学年份为当前年份
    document.getElementById('enrollmentYear').value = new Date().getFullYear();
}

// 关闭添加模态框
function closeAddModal() {
    const modal = document.getElementById('addModal');
    modal.style.display = 'none';
    document.getElementById('addStudentForm').reset();
}

// 点击模态框外部时关闭
window.onclick = function (event) {
    const modal = document.getElementById('addModal');
    if (event.target == modal) {
        closeAddModal();
    }
}

// 提交添加表单
function submitAdd() {
    const form = document.getElementById('addStudentForm');
    const formData = {
        studentName: form.studentName.value.trim(),
        gender: form.gender.value,
        enrollmentYear: parseInt(form.enrollmentYear.value),
        classId: form.classId.value,
        phoneNumber: form.phoneNumber.value.trim(),
        idNumber: form.idNumber.value.trim()
    };

    // 表单验证
    if (!validateAddForm(formData)) {
        return;
    }

    // 发送添加请求
    fetch(`${contextPath}/dept/student?action=add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('添加成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(data.message || '添加失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误');
        });
}

// 表单验证
function validateAddForm(formData) {
    if (!formData.studentName.trim()) {
        alert('请输入姓名');
        return false;
    }

    if (!formData.phoneNumber.trim()) {
        alert('请输入手机号');
        return false;
    }

    if (!formData.idNumber.trim()) {
        alert('请输入身份证号');
        return false;
    }

    // 手机号格式验证
    if (!/^1[3-9]\d{9}$/.test(formData.phoneNumber)) {
        alert('请输入正确的手机号');
        return false;
    }

    // 身份证号格式验证
    if (!/^\d{17}[\dXx]$/.test(formData.idNumber)) {
        alert('请输入正确的身份证号');
        return false;
    }

    // 入学年份验证
    const currentYear = new Date().getFullYear();
    if (formData.enrollmentYear < 2000 || formData.enrollmentYear > currentYear) {
        alert('请输入有效的入学年份');
        return false;
    }

    return true;
} 