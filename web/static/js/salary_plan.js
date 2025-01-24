// 显示编辑工资模态框
function showEditModal(roleId, roleName, baseSalary) {
    console.log('showEditModal called:', { roleId, roleName, baseSalary });
    // 检查元素是否存在
    const modal = document.getElementById('editSalaryModal');
    const roleIdInput = document.getElementById('roleId');
    const roleNameInput = document.getElementById('roleName');
    const baseSalaryInput = document.getElementById('baseSalary');

    if (!modal || !roleIdInput || !roleNameInput || !baseSalaryInput) {
        console.error('Required elements not found:', {
            modal: !!modal,
            roleIdInput: !!roleIdInput,
            roleNameInput: !!roleNameInput,
            baseSalaryInput: !!baseSalaryInput
        });
        return;
    }

    roleIdInput.value = roleId;
    roleNameInput.value = roleName;
    baseSalaryInput.value = baseSalary;
    modal.style.display = 'block';
}

// 关闭编辑工资模态框
function closeEditModal() {
    document.getElementById('editSalaryModal').style.display = 'none';
    document.getElementById('editSalaryForm').reset();
}

// 提交编辑工资表单
function submitEditSalary() {
    console.log('submitEditSalary called');
    const form = document.getElementById('editSalaryForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    console.log('Form data:', data);

    // 表单验证
    if (!validateSalaryForm(data)) {
        return;
    }

    // 显示加载状态
    const submitBtn = document.querySelector('.btn-submit');
    submitBtn.disabled = true;
    submitBtn.textContent = '保存中...';

    fetch(`${contextPath}/admin/salary/plan/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams(data)
    })
        .then(response => {
            console.log('Response:', response);
            return response.json();
        })
        .then(result => {
            console.log('Result:', result);
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
        })
        .finally(() => {
            // 恢复按钮状态
            submitBtn.disabled = false;
            submitBtn.textContent = '保存';
        });
}

// 表单验证
function validateSalaryForm(data) {
    const baseSalary = parseFloat(data.baseSalary);
    if (isNaN(baseSalary) || baseSalary <= 0) {
        alert('基本工资必须大于0');
        return false;
    }
    return true;
}

// 点击模态框外部关闭
window.onclick = function (event) {
    const modal = document.getElementById('editSalaryModal');
    if (event.target === modal) {
        closeEditModal();
    }
} 