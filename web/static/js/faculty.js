// 全选/取消全选
document.getElementById('selectAll').addEventListener('change', function () {
    const checkboxes = document.getElementsByName('facultyIds');
    checkboxes.forEach(checkbox => {
        checkbox.checked = this.checked;
    });
});

// 显示编辑成员模态框
function showEditModal(facultyId) {
    // 获取教职工详情
    fetch(`${contextPath}/admin/faculty/detail?facultyId=${facultyId}`)
        .then(response => response.json())
        .then(data => {
            console.log('获取到的教职工数据:', data);
            if (data.success) {
                const faculty = data.data;
                console.log('教职工详情:', faculty);
                // 填充表单数据
                document.getElementById('editFacultyId').value = faculty.facultyId;
                document.getElementById('editFacultyNumber').value = faculty.facultyNumber;
                document.getElementById('editFacultyName').value = faculty.facultyName;
                document.getElementById('editPassword').value = faculty.password;
                document.getElementById('editPhoneNumber').value = faculty.phoneNumber;
                document.getElementById('editDepartmentId').value = faculty.departmentId;
                document.getElementById('editIdNumber').value = faculty.idNumber;
                document.getElementById('editRoleId').value = faculty.roleId;

                // 显示模态框
                document.getElementById('editFacultyModal').style.display = 'block';
            } else {
                alert(data.message || '获取教职工信息失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取教职工信息失败，请稍后重试');
        });
}

// 关闭编辑模态框
function closeEditModal() {
    document.getElementById('editFacultyModal').style.display = 'none';
    document.getElementById('editFacultyForm').reset();
}

// 提交编辑表单
function submitEditFaculty() {
    const form = document.getElementById('editFacultyForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 工号和 facultyId 保持一致
    data.facultyId = document.getElementById('editFacultyId').value;

    // 表单验证
    if (!validateEditForm(data)) {
        return;
    }

    fetch(`${contextPath}/admin/faculty/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('更新成功');
                closeEditModal();
                window.location.reload();
            } else {
                alert(result.message || '更新失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('更新失败，请稍后重试');
        });
}

// 表单验证
function validateEditForm(data) {
    // 手机号验证
    const phonePattern = /^1[3-9]\d{9}$/;
    if (!phonePattern.test(data.phoneNumber)) {
        alert('请输入有效的手机号');
        return false;
    }

    // 身份证号验证
    const idNumberPattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (!idNumberPattern.test(data.idNumber)) {
        alert('请输入有效的身份证号');
        return false;
    }

    return true;
}

// 删除单个成员
function deleteFaculty(facultyId) {
    if (!confirm('确定要将该成员设置为离职状态吗？')) {
        return;
    }

    fetch(`${contextPath}/admin/faculty/delete?facultyId=${facultyId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('已设置为离职状态');
                window.location.reload();
            } else {
                alert(result.message || '设置离职状态失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('设置离职状态失败，请稍后重试');
        });
}

// 批量删除成员
function batchDelete() {
    const checkboxes = document.getElementsByName('facultyIds');
    const selectedIds = Array.from(checkboxes)
        .filter(cb => cb.checked)
        .map(cb => parseInt(cb.value));

    if (selectedIds.length === 0) {
        alert('请选择要设置为离职的成员');
        return;
    }

    if (confirm(`确定要将选中的 ${selectedIds.length} 个成员设置为离职状态吗？`)) {
        fetch(`${contextPath}/admin/faculty/batchDelete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedIds)
        })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert('批量设置离职状态成功');
                    window.location.reload();
                } else {
                    alert(result.message || '批量设置离职状态失败');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('批量设置离职状态失败，请稍后重试');
            });
    }
}

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase().trim();
    const tbody = document.querySelector('.faculty-table tbody');
    const rows = tbody.getElementsByTagName('tr');
    let hasResults = false;

    // 移除现有的"无搜索结果"提示
    const existingNoResults = document.querySelector('.no-results');
    if (existingNoResults) {
        existingNoResults.remove();
    }

    for (let row of rows) {
        const facultyId = row.cells[1].textContent.toLowerCase();
        const facultyName = row.cells[2].textContent.toLowerCase();
        const phoneNumber = row.cells[3].textContent.toLowerCase();
        const department = row.cells[4].textContent.toLowerCase();

        if (facultyId.includes(searchText) ||
            facultyName.includes(searchText) ||
            phoneNumber.includes(searchText) ||
            department.includes(searchText)) {
            row.style.display = '';
            hasResults = true;
        } else {
            row.style.display = 'none';
        }
    }

    // 如果没有搜索结果，显示提示信息
    if (!hasResults && searchText) {
        const noResults = document.createElement('div');
        noResults.className = 'no-results';
        noResults.textContent = '未找到匹配的教职工信息';
        tbody.parentNode.insertAdjacentElement('afterend', noResults);
    }

    // 更新全选框状态
    updateSelectAllCheckbox();
} 