document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const facultyId = document.querySelector('input[name="facultyId"]').value;
    const password = document.querySelector('input[name="password"]').value;

    // 简单的前端验证
    if (!facultyId || !password) {
        document.getElementById('errorMsg').textContent = '请填写所有字段';
        return;
    }

    // 教职工编号格式验证（假设是4位数字）
    const facultyIdPattern = /^\d{4}$/;
    if (!facultyIdPattern.test(facultyId)) {
        document.getElementById('errorMsg').textContent = '请输入有效的教职工编号';
        return;
    }

    // 提交表单
    this.submit();
}); 