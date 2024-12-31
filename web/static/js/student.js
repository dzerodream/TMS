// 显示添加学生模态框
function showAddModal() {
    document.getElementById('addStudentModal').style.display = 'block';
}

// 关闭添加学生模态框
function closeAddModal() {
    document.getElementById('addStudentModal').style.display = 'none';
    document.getElementById('addStudentForm').reset();
}

// 提交添加学生表单
function submitAddStudent() {
    const form = document.getElementById('addStudentForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    console.log('Sending data:', data);

    // 表单验证
    if (!validateStudentForm(data)) {
        return;
    }

    // 构造要发送的数据
    const studentData = {
        studentId: data.studentId.trim(),
        studentName: data.studentName.trim(),
        gender: data.gender,
        idNumber: data.idNumber.trim(),
        phoneNumber: data.phoneNumber.trim(),
        departmentId: data.departmentId,
        classId: data.classId,
        status: 1,
        enrollmentYear: new Date().getFullYear()
    };

    console.log('Sending student data:', studentData);

    fetch(`${contextPath}/admin/student/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(studentData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Response:', result);
            if (result.success) {
                alert('添加学生成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加学生失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('添加学生失败，请稍后重试');
        });
}

// 表单验证
function validateStudentForm(data) {
    // 学号验证
    if (!data.studentId || data.studentId.trim() === '') {
        alert('请输入学号');
        return false;
    }

    // 姓名验证
    if (!data.studentName || data.studentName.trim() === '') {
        alert('请输入姓名');
        return false;
    }

    // 性别验证
    if (!data.gender) {
        alert('请选择性别');
        return false;
    }

    // 身份证号验证
    if (!data.idNumber || !/^\d{17}[\dXx]$/.test(data.idNumber.trim())) {
        alert('请输入正确的18位身份证号');
        return false;
    }

    // 手机号验证
    if (!data.phoneNumber || !/^1[3-9]\d{9}$/.test(data.phoneNumber.trim())) {
        alert('请输入正确的11位手机号');
        return false;
    }

    // 院系验证
    if (!data.departmentId) {
        alert('请选择院系');
        return false;
    }

    // 班级验证
    if (!data.classId) {
        alert('请选择班级');
        return false;
    }

    return true;
}

// 删除学生
function deleteStudent(studentId) {
    if (!studentId) {
        alert('学号不能为空');
        return;
    }

    if (!confirm('确定要删除该学生吗？此操作不可恢复。')) {
        return;
    }

    console.log('Deleting student:', studentId);

    fetch(`${contextPath}/admin/student/delete?studentId=${studentId}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Delete result:', result);
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

// 批量删除
function batchDelete() {
    const checkboxes = document.getElementsByName('studentIds');
    const selectedIds = Array.from(checkboxes)
        .filter(cb => cb.checked)
        .map(cb => cb.value);

    if (selectedIds.length === 0) {
        alert('请选择要删除的学生');
        return;
    }

    if (!confirm(`确定要删除选中的 ${selectedIds.length} 个学生吗？此操作不可恢复。`)) {
        return;
    }

    console.log('Batch deleting students:', selectedIds);

    fetch(`${contextPath}/admin/student/batchDelete`, {
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
            console.log('Batch delete result:', result);
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

// 全选/取消全选
function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.getElementsByName('studentIds');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAll.checked;
    });
}

// 处理搜索功能
function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchText = searchInput.value.toLowerCase();
    const tbody = document.querySelector('.student-table tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let row of rows) {
        const name = row.cells[1].textContent.toLowerCase();
        const number = row.cells[2].textContent.toLowerCase();
        if (name.includes(searchText) || number.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
}

// 显示详情模态框
function showDetailModal(studentId) {
    console.log('Fetching details for student:', studentId);

    fetch(`${contextPath}/admin/student/detail?studentId=${studentId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(student => {
            console.log('Parsed student data:', student);

            // 填充表单
            document.getElementById('detail_studentId').value = student.studentId;
            document.getElementById('detail_studentName').value = student.studentName;
            document.getElementById('detail_gender').value = student.gender;
            document.getElementById('detail_idNumber').value = student.idNumber;
            document.getElementById('detail_phoneNumber').value = student.phoneNumber;
            document.getElementById('detail_departmentId').value = student.departmentId;

            // 更新班级选项
            return updateClassOptions(student.departmentId, 'detail_classId')
                .then(() => {
                    document.getElementById('detail_classId').value = student.classId;
                    // 显示模态框
                    document.getElementById('detailStudentModal').style.display = 'block';
                });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取学生信息失败: ' + error.message);
        });
}

// 关闭详情模态框
function closeDetailModal() {
    document.getElementById('detailStudentModal').style.display = 'none';
}

// 提交更新学生信息
function submitUpdateStudent() {
    const form = document.getElementById('detailStudentForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateStudentForm(data)) {
        return;
    }

    // 构造要发送的数据
    const studentData = {
        studentId: data.studentId.trim(),
        studentName: data.studentName.trim(),
        gender: data.gender,
        idNumber: data.idNumber.trim(),
        phoneNumber: data.phoneNumber.trim(),
        departmentId: data.departmentId,
        classId: data.classId
    };

    fetch(`${contextPath}/admin/student/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(studentData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('修改成功');
                closeDetailModal();
                window.location.reload();
            } else {
                alert(result.message || '修改失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('修改失败，请稍后重试');
        });
}

// 切换学生状态
function toggleStudentStatus(studentId, currentStatus) {
    const newStatus = currentStatus === 1 ? 0 : 1;
    const statusText = newStatus === 1 ? '在校' : '毕业';

    if (!confirm(`确定要将该学生状态改为${statusText}吗？`)) {
        return;
    }

    console.log('Toggling student status:', studentId, 'to', newStatus);

    fetch(`${contextPath}/admin/student/toggleStatus`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            studentId: studentId,
            status: newStatus
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(result => {
            console.log('Toggle status result:', result);
            if (result.success) {
                alert('状态修改成功');
                window.location.reload();
            } else {
                alert(result.message || '状态修改失败');
            }
        })
        .catch(error => {
            console.error('Toggle status error:', error);
            alert('状态修改失败，请稍后重试');
        });
}

// 初始化院系选择变化监听
function initDepartmentChange() {
    // 为添加表单中的院系选择添加监听
    document.getElementById('departmentId').addEventListener('change', function () {
        updateClassOptions(this.value, 'classId');
    });

    // 为详情表单中的院系选择添加监听
    document.getElementById('detail_departmentId').addEventListener('change', function () {
        updateClassOptions(this.value, 'detail_classId');
    });
}

// 更新班级选项
function updateClassOptions(departmentId, classSelectId) {
    if (!departmentId) {
        clearClassOptions(classSelectId);
        return Promise.resolve();
    }

    return fetch(`${contextPath}/admin/student/classes?departmentId=${departmentId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(classes => {
            const classSelect = document.getElementById(classSelectId);
            clearClassOptions(classSelectId);

            classes.forEach(classInfo => {
                const option = new Option(classInfo.className, classInfo.classId);
                classSelect.add(option);
            });
        })
        .catch(error => {
            console.error('Error fetching classes:', error);
            alert('获取班级列表失败');
            throw error; // 继续抛出错误以便上层处理
        });
}

// 清空班级选项
function clearClassOptions(classSelectId) {
    const classSelect = document.getElementById(classSelectId);
    classSelect.innerHTML = '<option value="">请选择班级</option>';
}

// 在页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function () {
    initDepartmentChange();
}); 