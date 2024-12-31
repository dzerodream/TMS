// 显示添加院系模态框
function showAddModal() {
    document.getElementById('addDepartmentModal').style.display = 'block';
}

// 关闭添加院系模态框
function closeAddModal() {
    document.getElementById('addDepartmentModal').style.display = 'none';
    document.getElementById('addDepartmentForm').reset();
    clearFormErrors('addDepartmentForm');
}

// 显示编辑院系模态框
function showEditModal(deptId) {
    // 获取院系详情
    fetch(`${contextPath}/admin/department/detail?deptId=${deptId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const dept = data.data;
                document.getElementById('editDeptId').value = dept.deptId;
                document.getElementById('editDeptName').value = dept.deptName;
                document.getElementById('editDepartmentModal').style.display = 'block';
            } else {
                alert(data.message || '获取院系信息失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取院系信息失败，请稍后重试');
        });
}

// 关闭编辑院系模态框
function closeEditModal() {
    document.getElementById('editDepartmentModal').style.display = 'none';
    document.getElementById('editDepartmentForm').reset();
    clearFormErrors('editDepartmentForm');
}

// 提交添加院系表单
function submitAddDepartment() {
    const form = document.getElementById('addDepartmentForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateDepartmentForm(data, 'addDepartmentForm')) {
        return;
    }

    fetch(`${contextPath}/admin/department/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('添加院系成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加院系失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加院系失败，请稍后重试');
        });
}

// 提交编辑院系表单
function submitEditDepartment() {
    const form = document.getElementById('editDepartmentForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateDepartmentForm(data, 'editDepartmentForm')) {
        return;
    }

    fetch(`${contextPath}/admin/department/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('更新院系成功');
                closeEditModal();
                window.location.reload();
            } else {
                alert(result.message || '更新院系失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('更新院系失败，请稍后重试');
        });
}

// 删除院系
function deleteDepartment(deptId) {
    if (!confirm('确定要删除该院系吗？删除后不可恢复！')) {
        return;
    }

    fetch(`${contextPath}/admin/department/delete?deptId=${deptId}`, {
        method: 'POST'
    })
        .then(response => response.json())
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

// 表单验证
function validateDepartmentForm(data, formId) {
    let isValid = true;
    const form = document.getElementById(formId);

    // 清除之前的错误提示
    clearFormErrors(formId);

    // 院系编号验证(仅添加时验证)
    if (formId === 'addDepartmentForm') {
        const deptIdPattern = /^[A-Z0-9]{2,10}$/;
        if (!deptIdPattern.test(data.deptId)) {
            showError(form, 'deptId', '院系编号格式不正确（2-10位大写字母和数字）');
            isValid = false;
        }
    }

    // 院系名称验证
    if (!data.deptName || data.deptName.length < 2 || data.deptName.length > 100) {
        showError(form, 'deptName', '请输入正确的院系名称（2-100个字符）');
        isValid = false;
    }

    return isValid;
}

// 显示错误提示
function showError(form, fieldName, message) {
    const field = form.querySelector(`[name="${fieldName}"]`);
    const formGroup = field.closest('.form-group');
    formGroup.classList.add('error');
    const errorMessage = formGroup.querySelector('.error-message');
    if (errorMessage) {
        errorMessage.textContent = message;
    }
}

// 清除表单错误提示
function clearFormErrors(formId) {
    const form = document.getElementById(formId);
    form.querySelectorAll('.form-group').forEach(group => {
        group.classList.remove('error');
    });
}

// 关闭模态框的其他方式
window.onclick = function (event) {
    const addModal = document.getElementById('addDepartmentModal');
    const editModal = document.getElementById('editDepartmentModal');
    if (event.target === addModal) {
        closeAddModal();
    }
    if (event.target === editModal) {
        closeEditModal();
    }
}

// 更新院系人数
function updateDepartmentCount() {
    fetch(`${contextPath}/admin/department/updateCount`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('院系人数更新成功');
                window.location.reload();
            } else {
                alert(result.message || '院系人数更新失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('院系人数更新失败，请稍后重试');
        });
}

// 全选/取消全选
function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByName('deptIds');

    for (let checkbox of checkboxes) {
        // 只选中可见行的复选框
        if (checkbox.closest('tr').style.display !== 'none') {
            checkbox.checked = selectAll.checked;
        }
    }
}

// 批量删除
function batchDelete() {
    const checkboxes = document.getElementsByName('deptIds');
    const selectedIds = Array.from(checkboxes)
        .filter(cb => cb.checked)
        .map(cb => cb.value);

    if (selectedIds.length === 0) {
        alert('请选择要删除的院系');
        return;
    }

    if (!confirm(`确定要删除选中的 ${selectedIds.length} 个院系吗？此操作不可恢复！`)) {
        return;
    }

    fetch(`${contextPath}/admin/department/batchDelete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedIds)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('批量删除成功');
                window.location.reload();
            } else {
                alert(result.message || '批量删除失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('批量删除失败，请稍后重试');
        });
}

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase().trim();
    const tbody = document.querySelector('.department-table tbody');
    const rows = tbody.getElementsByTagName('tr');
    let hasResults = false;

    // 移除现有的"无搜索结果"提示
    const existingNoResults = document.querySelector('.no-results');
    if (existingNoResults) {
        existingNoResults.remove();
    }

    for (let row of rows) {
        const deptId = row.cells[1].textContent.toLowerCase();
        const deptName = row.cells[2].textContent.toLowerCase();
        const adminName = row.cells[4].textContent.toLowerCase();

        if (deptId.includes(searchText) ||
            deptName.includes(searchText) ||
            adminName.includes(searchText)) {
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
        noResults.textContent = '未找到匹配的院系信息';
        tbody.parentNode.insertAdjacentElement('afterend', noResults);
    }

    // 更新全选框状态
    updateSelectAllCheckbox();
}

// 更新全选框状态
function updateSelectAllCheckbox() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByName('deptIds');
    const visibleCheckboxes = Array.from(checkboxes).filter(cb =>
        cb.closest('tr').style.display !== 'none'
    );

    if (visibleCheckboxes.length === 0) {
        selectAll.checked = false;
        selectAll.disabled = true;
    } else {
        selectAll.disabled = false;
        selectAll.checked = visibleCheckboxes.every(cb => cb.checked);
    }
}