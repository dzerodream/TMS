function showAddModal() {
    document.getElementById('addFacultyModal').style.display = 'block';
}

function closeAddModal() {
    document.getElementById('addFacultyModal').style.display = 'none';
    document.getElementById('addFacultyForm').reset();
}

function submitAddFaculty() {
    const form = document.getElementById('addFacultyForm');
    if (!form) {
        console.error('找不到表单元素');
        return;
    }

    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // 表单验证
    if (!validateForm(data)) {
        return;
    }

    console.log('提交的数据:', data);
    console.log('contextPath:', contextPath); // 检查contextPath是否正确

    // 检查必填字段
    if (!data.facultyName || !data.departmentId || !data.phoneNumber || !data.idNumber || !data.password) {
        alert('请填写所有必填字段');
        return;
    }

    const requestData = {
        facultyName: data.facultyName,
        departmentId: data.departmentId,
        phoneNumber: data.phoneNumber,
        idNumber: data.idNumber,
        password: data.password
    };

    console.log('发送的JSON数据:', JSON.stringify(requestData));

    fetch(`${contextPath}/admin/faculty/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            console.log('响应状态:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(result => {
            console.log('响应结果:', result);
            if (result.success) {
                alert('添加成员成功');
                closeAddModal();
                window.location.reload();
            } else {
                alert(result.message || '添加成员失败');
            }
        })
        .catch(error => {
            console.error('错误:', error);
            alert('添加成员失败，请稍后重试\n错误信息: ' + error.message);
        });
}

function validateForm(data) {
    console.log('开始验证表单数据:', data); // 添加调试信息

    // 身份证号验证
    const idNumberPattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (!idNumberPattern.test(data.idNumber)) {
        alert('请输入有效的身份证号');
        return false;
    }

    // 手机号验证
    const phonePattern = /^1[3-9]\d{9}$/;
    if (!phonePattern.test(data.phoneNumber)) {
        alert('请输入有效的手机号');
        return false;
    }

    // 其他字段验证
    if (!data.facultyName || !data.departmentId || !data.password) {
        alert('请填写所有必填字段');
        return false;
    }

    console.log('表单验证通过'); // 添加调试信息
    return true;
}

// 关闭模态框的其他方式
window.onclick = function (event) {
    const modal = document.getElementById('addFacultyModal');
    if (event.target === modal) {
        closeAddModal();
    }
} 