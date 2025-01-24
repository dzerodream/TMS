function submitUpdate() {
    try {
        console.log('submitUpdate function called');

        const form = document.getElementById('profileForm');
        if (!form) {
            console.error('Form not found!');
            return;
        }

        const data = {
            phoneNumber: form.phoneNumber.value.trim(),
            idNumber: form.idNumber.value.trim(),
            password: form.password.value.trim()
        };

        console.log('Form data:', data);

        // 添加基本验证
        if (!data.phoneNumber || !data.idNumber) {
            alert('请填写必填信息');
            return;
        }

        if (!contextPath) {
            console.error('contextPath is not defined!');
            alert('系统错误：contextPath未定义');
            return;
        }

        const updateUrl = `${contextPath}/teacher/profile/update`;
        console.log('Sending request to:', updateUrl);

        fetch(updateUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(data),
            credentials: 'same-origin'
        })
            .then(response => {
                console.log('Response received:', response);
                console.log('Response status:', response.status);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(result => {
                console.log('Response data:', result);
                if (result.success) {
                    alert('更新成功');
                    if (data.password) {
                        window.location.href = `${contextPath}/logout`;
                    } else {
                        window.location.reload();
                    }
                } else {
                    alert(result.message || '更新失败');
                }
            })
            .catch(error => {
                console.error('Error details:', error);
                alert('系统错误，请稍后重试');
            });
    } catch (error) {
        console.error('Unexpected error:', error);
        alert('发生意外错误，请查看控制台');
    }
}

// DOM加载完成后添加事件监听器
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('profileForm');
    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            submitUpdate();
        });
    }
}); 