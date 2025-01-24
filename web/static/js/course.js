// 全选/取消全选
function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByClassName('course-checkbox');

    for (let checkbox of checkboxes) {
        checkbox.checked = selectAll.checked;
    }
}

// 批量删除
function batchDelete() {
    const checkboxes = document.getElementsByClassName('course-checkbox');
    const selectedIds = [];

    for (let checkbox of checkboxes) {
        if (checkbox.checked) {
            selectedIds.push(checkbox.value);
        }
    }

    if (selectedIds.length === 0) {
        alert('请选择要删除的课程');
        return;
    }

    if (!confirm(`确定要删除选中的 ${selectedIds.length} 个课程吗？此操作不可恢复。`)) {
        return;
    }

    fetch(`${contextPath}/admin/course/batchDelete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedIds)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
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

// 显示添加课程模态框
function showAddModal() {
    document.getElementById('addCourseModal').style.display = 'block';
}

// 关闭添加课程模态框
function closeAddModal() {
    document.getElementById('addCourseModal').style.display = 'none';
    document.getElementById('addCourseForm').reset();
}

// 提交添加课程表单
function submitAddCourse() {
    const form = document.getElementById('addCourseForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateCourseForm(data)) {
        return;
    }

    // 转换数据类型
    data.credits = parseFloat(data.credits);
    data.totalHours = parseInt(data.totalHours);

    fetch(`${contextPath}/admin/course/add`, {
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
                alert('添加课程成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加课程失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加课程失败，请稍后重试');
        });
}

// 表单验证
function validateCourseForm(data) {
    if (!data.courseId || data.courseId.trim() === '') {
        alert('请输入课程编号');
        return false;
    }

    if (!data.courseName || data.courseName.trim() === '') {
        alert('请输入课程名称');
        return false;
    }

    if (!data.deptId) {
        alert('请选择所属院系');
        return false;
    }

    if (!data.credits || data.credits <= 0) {
        alert('请输入有效的学分');
        return false;
    }

    if (!data.totalHours || data.totalHours <= 0) {
        alert('请输入有效的总学时');
        return false;
    }

    if (!data.courseNature) {
        alert('请选择课程性质');
        return false;
    }

    if (!data.assessmentMethod) {
        alert('请选择考核方式');
        return false;
    }

    return true;
}

// 显示编辑课程模态框
function showEditModal(courseId) {
    // 获取课程详情
    fetch(`${contextPath}/admin/course/detail?courseId=${courseId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(course => {
            // 填充表单
            document.getElementById('edit_courseId').value = course.courseId;
            document.getElementById('edit_courseName').value = course.courseName;
            document.getElementById('edit_deptId').value = course.deptId;
            document.getElementById('edit_credits').value = course.credits;
            document.getElementById('edit_totalHours').value = course.totalHours;
            document.getElementById('edit_courseNature').value = course.courseNature;
            document.getElementById('edit_assessmentMethod').value = course.assessmentMethod;

            // 显示模态框
            document.getElementById('editCourseModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取课程信息失败，请稍后重试');
        });
}

// 关闭编辑课程模态框
function closeEditModal() {
    document.getElementById('editCourseModal').style.display = 'none';
    document.getElementById('editCourseForm').reset();
}

// 提交编辑课程表单
function submitEditCourse() {
    const form = document.getElementById('editCourseForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateCourseForm(data)) {
        return;
    }

    // 转换数据类型
    data.credits = parseFloat(data.credits);
    data.totalHours = parseInt(data.totalHours);

    fetch(`${contextPath}/admin/course/update`, {
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
                alert('修改课程成功');
                closeEditModal();
                window.location.reload();
            } else {
                alert(result.message || '修改课程失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('修改课程失败，请稍后重试');
        });
}

// 删除课程
function deleteCourse(courseId) {
    if (!confirm('确定要删除该课程吗？此操作不可恢复。')) {
        return;
    }

    fetch(`${contextPath}/admin/course/delete?courseId=${courseId}`, {
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
    const searchText = searchInput.value.toLowerCase().trim();
    const tbody = document.querySelector('.course-table tbody');
    const rows = tbody.getElementsByTagName('tr');
    const noResult = document.getElementById('noResult');

    let hasResults = false;

    // 如果搜索框为空，显示所有行并清除高亮
    if (searchText === '') {
        Array.from(rows).forEach(row => {
            row.style.display = '';
            clearHighlight(row);
        });
        noResult.style.display = 'none';
        return;
    }

    for (let row of rows) {
        const courseId = row.cells[1].textContent;
        const courseName = row.cells[2].textContent;
        const departmentName = row.cells[3].textContent;

        // 搜索课程编号、课程名称或院系名称
        if (courseId.toLowerCase().includes(searchText) ||
            courseName.toLowerCase().includes(searchText) ||
            departmentName.toLowerCase().includes(searchText)) {
            row.style.display = '';
            hasResults = true;

            // 只高亮前三列（跳过复选框列和操作列）
            highlightText(row.cells[1], courseId, searchText);
            highlightText(row.cells[2], courseName, searchText);
            highlightText(row.cells[3], departmentName, searchText);
        } else {
            row.style.display = 'none';
            clearHighlight(row);
        }
    }

    // 显示或隐藏无结果提示
    noResult.style.display = hasResults ? 'none' : 'block';
}

// 高亮匹配文本
function highlightText(cell, text, searchText) {
    // 如果单元格包含操作按钮，则不进行高亮处理
    if (cell.querySelector('.action-link')) {
        return;
    }

    if (text.toLowerCase().includes(searchText.toLowerCase())) {
        const regex = new RegExp(`(${searchText})`, 'gi');
        cell.innerHTML = text.replace(regex, '<span class="highlight">$1</span>');
    }
}

// 清除高亮
function clearHighlight(row) {
    const cells = row.getElementsByTagName('td');
    for (let i = 1; i < cells.length - 1; i++) { // 跳过第一列（复选框）和最后一列（操作按钮）
        const cell = cells[i];
        // 如果单元格不包含操作按钮，则清除高亮
        if (!cell.querySelector('.action-link')) {
            const text = cell.textContent;
            cell.innerHTML = text;
        }
    }
}

// 添加防抖功能，避免频繁搜索
function debounce(func, wait) {
    let timeout;
    return function () {
        const context = this;
        const args = arguments;
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            func.apply(context, args);
        }, wait);
    };
}

// 使用防抖优化搜索
const debouncedSearch = debounce(handleSearch, 300);

// 添加事件监听
document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');

    // 输入时触发搜索
    searchInput.addEventListener('input', debouncedSearch);

    // 回车时立即搜索
    searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });

    // 点击搜索图标时搜索
    document.querySelector('.search-icon').addEventListener('click', handleSearch);
}); 