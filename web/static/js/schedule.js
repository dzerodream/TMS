// 确保在全局范围内可以访问
window.changeWeek = function (week) {
    if (!week) {
        console.error('No week value provided');
        return;
    }
    console.log('Changing week to:', week);
    try {
        const url = contextPath + '/teacher/schedule?week=' + week;
        console.log('Redirecting to:', url);
        window.location.href = url;
    } catch (error) {
        console.error('Error changing week:', error);
    }
};

// 显示添加课程安排模态框
function showAddModal() {
    console.log('Showing add modal');
    const modal = document.getElementById('addScheduleModal');
    if (modal) {
        modal.style.display = 'block';
    } else {
        console.error('Modal not found');
    }
}

// 关闭添加课程安排模态框
function closeAddModal() {
    console.log('Closing add modal');
    const modal = document.getElementById('addScheduleModal');
    if (modal) {
        modal.style.display = 'none';
        document.getElementById('addScheduleForm').reset();
    }
}

// 处理上课对象类型切换
function handleTargetTypeChange() {
    console.log('Target type changed');
    const targetType = document.getElementById('targetType').value;
    const classSelection = document.getElementById('classSelection');
    const studentSelection = document.getElementById('studentSelection');

    if (classSelection && studentSelection) {
        classSelection.style.display = targetType === 'class' ? 'block' : 'none';
        studentSelection.style.display = targetType === 'student' ? 'block' : 'none';
    }
}

// 提交添加课程安排
function submitAddSchedule() {
    console.log('Submitting schedule');
    const form = document.getElementById('addScheduleForm');
    if (!form) {
        console.error('Form not found');
        return;
    }

    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 获取选中的上课对象
    const targetType = document.getElementById('targetType').value;
    const targetSelect = document.getElementById(targetType === 'class' ? 'classIds' : 'studentIds');
    if (!targetSelect) {
        console.error('Target select not found');
        return;
    }

    const targetIds = Array.from(targetSelect.selectedOptions).map(option => option.value);
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

    console.log('Sending request:', requestData);

    fetch(`${contextPath}/admin/schedule/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            console.log('Response status:', response.status);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Response:', result);
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

// 删除单个课程安排
function deleteSchedule(scheduleId) {
    console.log('Deleting schedule:', scheduleId);
    if (!confirm('确定要删除该课程安排吗？')) {
        return;
    }

    fetch(`${contextPath}/admin/schedule/delete?scheduleId=${scheduleId}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            console.log('Delete response status:', response.status);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Delete response:', result);
            if (result.success) {
                alert('删除成功');
                window.location.reload();
            } else {
                alert(result.message || '删除失败');
            }
        })
        .catch(error => {
            console.error('Delete error:', error);
            alert('删除失败，请稍后重试');
        });
}

// 全选/取消全选
function toggleSelectAll() {
    console.log('Toggle select all');
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByClassName('schedule-checkbox');

    if (selectAll) {
        Array.from(checkboxes).forEach(checkbox => {
            checkbox.checked = selectAll.checked;
        });
    }
}

// 批量删除
function batchDelete() {
    console.log('Starting batch delete');
    const checkboxes = document.getElementsByClassName('schedule-checkbox');
    const selectedIds = Array.from(checkboxes)
        .filter(checkbox => checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    console.log('Selected IDs:', selectedIds);

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
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(selectedIds)
    })
        .then(response => {
            console.log('Batch delete response status:', response.status);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Batch delete response:', result);
            if (result.success) {
                alert('批量删除成功');
                window.location.reload();
            } else {
                alert(result.message || '批量删除失败');
            }
        })
        .catch(error => {
            console.error('Batch delete error:', error);
            alert('批量删除失败，请稍后重试');
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

// 页面加载完成后的初始化
document.addEventListener('DOMContentLoaded', function () {
    console.log('Page loaded, initializing...');

    // 初始化全选checkbox
    const selectAll = document.getElementById('selectAll');
    if (selectAll) {
        selectAll.addEventListener('change', toggleSelectAll);
    }

    // 初始化所有删除按钮
    const deleteButtons = document.querySelectorAll('.action-link.delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const scheduleId = this.getAttribute('data-id');
            if (scheduleId) {
                deleteSchedule(parseInt(scheduleId));
            }
        });
    });

    // 初始化搜索功能
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', handleSearch);
    }

    // 初始化上课对象类型切换
    const targetType = document.getElementById('targetType');
    if (targetType) {
        targetType.addEventListener('change', handleTargetTypeChange);
        // 初始化显示状态
        handleTargetTypeChange();
    }
});

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    if (!searchInput) return;

    const searchText = searchInput.value.toLowerCase();
    const tbody = document.querySelector('.schedule-table tbody');
    if (!tbody) return;

    const rows = tbody.getElementsByTagName('tr');
    for (let row of rows) {
        const courseName = row.cells[1].textContent.toLowerCase();
        const teacherName = row.cells[2].textContent.toLowerCase();
        if (courseName.includes(searchText) || teacherName.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
} 