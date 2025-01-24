// 搜索功能
function handleSearch() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('.schedule-table tbody tr');

    rows.forEach(row => {
        const courseName = row.cells[1].textContent.toLowerCase();
        const facultyName = row.cells[2].textContent.toLowerCase();
        const shouldShow = courseName.includes(searchText) || facultyName.includes(searchText);
        row.style.display = shouldShow ? '' : 'none';
    });
}

// 显示添加模态框
function showAddModal() {
    document.getElementById('addScheduleModal').style.display = 'block';
    document.getElementById('addScheduleForm').reset();
}

// 关闭添加模态框
function closeAddModal() {
    document.getElementById('addScheduleModal').style.display = 'none';
}

// 切换上课对象类型
function handleTargetTypeChange() {
    const targetType = document.getElementById('targetType').value;
    document.getElementById('classSelection').style.display = targetType === 'class' ? 'block' : 'none';
    document.getElementById('studentSelection').style.display = targetType === 'student' ? 'block' : 'none';
}

// 提交添加课程安排
function submitAddSchedule() {
    const form = document.getElementById('addScheduleForm');
    const targetType = document.getElementById('targetType').value;
    const targets = targetType === 'class' ?
        Array.from(document.getElementById('classIds').selectedOptions).map(opt => opt.value) :
        Array.from(document.getElementById('studentIds').selectedOptions).map(opt => opt.value);

    if (targets.length === 0) {
        alert('请选择上课对象');
        return;
    }

    const formData = {
        courseId: form.courseId.value,
        facultyId: form.facultyId.value,
        startWeek: form.startWeek.value,
        endWeek: form.endWeek.value,
        weekDay: form.weekDay.value,
        classTime: form.classTime.value,
        location: form.location.value,
        targetType: targetType,
        targets: targets
    };

    fetch(`${contextPath}/dept/schedule/add`, {
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

// 删除课程安排
function deleteSchedule(scheduleId) {
    if (!confirm('确定要删除这个课程安排吗？')) {
        return;
    }

    fetch(`${contextPath}/dept/schedule/delete?scheduleId=${scheduleId}`, {
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

// 全选/取消全选
function toggleSelectAll() {
    const checkboxes = document.getElementsByName('scheduleIds');
    const selectAllCheckbox = document.getElementById('selectAll');
    checkboxes.forEach(checkbox => checkbox.checked = selectAllCheckbox.checked);
}

// 批量删除
function batchDelete() {
    const selectedIds = Array.from(document.getElementsByName('scheduleIds'))
        .filter(checkbox => checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    if (selectedIds.length === 0) {
        alert('请选择要删除的课程安排');
        return;
    }

    if (!confirm(`确定要删除选中的${selectedIds.length}个课程安排吗？`)) {
        return;
    }

    fetch(`${contextPath}/dept/schedule/batchDelete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedIds)
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