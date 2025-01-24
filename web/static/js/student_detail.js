// 提交更新
function submitUpdate() {
    const form = document.getElementById('studentDetailForm');
    const formData = {
        studentId: form.studentId.value,
        studentName: form.studentName.value.trim(),
        gender: form.gender.value,
        phoneNumber: form.phoneNumber.value.trim(),
        idNumber: form.idNumber.value.trim(),
        classId: form.classId.value,
        departmentId: '${student.departmentId}' // 保持原院系不变
    };

    // 表单验证
    if (!validateForm(formData)) {
        return;
    }

    // 发送更新请求
    fetch(`${contextPath}/dept/student/detail`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('更新成功');
                window.location.reload();
            } else {
                alert(data.message || '更新失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误');
        });
}

// 表单验证
function validateForm(formData) {
    if (!formData.studentName) {
        alert('请输入姓名');
        return false;
    }

    if (!formData.phoneNumber) {
        alert('请输入手机号');
        return false;
    }

    if (!formData.idNumber) {
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

    return true;
} 