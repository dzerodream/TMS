// 显示添加课程安排模态框
function showAddModal() {
    document.getElementById('addScheduleModal').style.display = 'block';
}

// 关闭添加课程安排模态框
function closeAddModal() {
    document.getElementById('addScheduleModal').style.display = 'none';
    document.getElementById('addScheduleForm').reset();
}

// 处理上课对象类型切换
function handleTargetTypeChange() {
    const targetType = document.getElementById('targetType').value;
    document.getElementById('classSelection').style.display = targetType === 'class' ? 'block' : 'none';
    document.getElementById('studentSelection').style.display = targetType === 'student' ? 'block' : 'none';
}

// 提交添加课程安排
function submitAddSchedule() {
    const form = document.getElementById('addScheduleForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 获取选中的上课对象
    const targetType = document.getElementById('targetType').value;
    const targetIds = Array.from(document.getElementById(targetType === 'class' ? 'classIds' : 'studentIds').selectedOptions)
        .map(option => option.value);

    if (targetIds.length === 0) {
        alert('请选择上课对象');
        return;
    }

    // 表单验证
    if (!validateScheduleForm(data)) {
        return;
    }

    // 构建请求数据
    const requestData = {
        schedule: data,
        targetIds: targetIds,
        targetType: targetType
    };

    fetch(`${contextPath}/admin/schedule/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('添加课程安排成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加课程安排失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加课程安排失败，请稍后重试');
        });
}

// 删除课程安排
function deleteSchedule(scheduleId) {
    if (!confirm('确定要删除该课程安排吗？')) {
        return;
    }

    fetch(`${contextPath}/admin/schedule/delete?scheduleId=${scheduleId}`, {
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
function validateScheduleForm(data) {
    if (!data.courseId) {
        alert('请选择课程');
        return false;
    }

    if (!data.facultyId) {
        alert('请选择任课教师');
        return false;
    }

    if (!data.startWeek || !data.endWeek || !data.weekDay || !data.classTime) {
        alert('请完整填写上课时间');
        return false;
    }

    if (parseInt(data.startWeek) > parseInt(data.endWeek)) {
        alert('起始周不能大于结束周');
        return false;
    }

    if (!data.location) {
        alert('请填写上课地点');
        return false;
    }

    return true;
}

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase();
    const tbody = document.querySelector('.schedule-table tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let row of rows) {
        const courseName = row.cells[0].textContent.toLowerCase();
        const teacherName = row.cells[3].textContent.toLowerCase();
        if (courseName.includes(searchText) || teacherName.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
}

// 全选/取消全选
function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByClassName('schedule-checkbox');

    for (let checkbox of checkboxes) {
        checkbox.checked = selectAll.checked;
    }
}

// 批量删除
function batchDelete() {
    const checkboxes = document.getElementsByClassName('schedule-checkbox');
    const selectedIds = [];

    for (let checkbox of checkboxes) {
        if (checkbox.checked) {
            selectedIds.push(checkbox.value);
        }
    }

    if (selectedIds.length === 0) {
        alert('请选择要删除的课程安排');
        return;
    }

    if (!confirm(`确定要删除选中的 ${selectedIds.length} 个课程安排吗？此操作不可恢复。`)) {
        return;
    }

    fetch(`${contextPath}/admin/schedule/batchDelete`, {
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