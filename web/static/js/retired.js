// 恢复离职员工
function restoreFaculty(facultyId) {
    if (!confirm('确定要恢复该员工的在职状态吗？')) {
        return;
    }

    fetch(`${contextPath}/admin/retired/restore?facultyId=${facultyId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('已恢复员工在职状态');
                window.location.reload();
            } else {
                alert(result.message || '恢复失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('恢复失败，请稍后重试');
        });
}

// 删除离职员工
function deleteRetiredFaculty(facultyId) {
    if (!confirm('确定要永久删除该离职员工吗？此操作不可恢复！')) {
        return;
    }

    fetch(`${contextPath}/admin/retired/delete?facultyId=${facultyId}`, {
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

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase().trim();
    const tbody = document.querySelector('.retired-table tbody');
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
        const department = row.cells[3].textContent.toLowerCase();
        const leaveDate = row.cells[4].textContent.toLowerCase();

        if (facultyId.includes(searchText) ||
            facultyName.includes(searchText) ||
            department.includes(searchText) ||
            leaveDate.includes(searchText)) {
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
        noResults.textContent = '未找到匹配的离职人员信息';
        tbody.parentNode.insertAdjacentElement('afterend', noResults);
    }

    // 更新全选框状态
    updateSelectAllCheckbox();
}

// 更新全选框状态
function updateSelectAllCheckbox() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByName('facultyIds');
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

// 修改全选/取消全选功能
function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByName('facultyIds');

    for (let checkbox of checkboxes) {
        // 只选中可见行的复选框
        if (checkbox.closest('tr').style.display !== 'none') {
            checkbox.checked = selectAll.checked;
        }
    }
}

// 批量删除离职员工
function batchDelete() {
    const checkboxes = document.getElementsByName('facultyIds');
    const selectedIds = Array.from(checkboxes)
        .filter(cb => cb.checked)
        .map(cb => parseInt(cb.value));

    if (selectedIds.length === 0) {
        alert('请选择要删除的离职员工');
        return;
    }

    if (!confirm(`确定要永久删除选中的 ${selectedIds.length} 个离职员工吗？此操作不可恢复！`)) {
        return;
    }

    fetch(`${contextPath}/admin/retired/batchDelete`, {
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