<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>教职工管理系统 - 基本信息</title>
        <script>
            var contextPath = '${pageContext.request.contextPath}';
        </script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/teacher.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/profile.css">
    </head>

    <body>
        <div class="admin-container">
            <jsp:include page="components/sidebar.jsp" />

            <div class="main-content">
                <div class="header">
                    <div class="page-title">基本信息</div>
                    <div class="user-info">
                        欢迎，${sessionScope.faculty.facultyName}
                        <span class="department-name">${sessionScope.faculty.departmentName}</span>
                        <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                    </div>
                </div>

                <div class="content">
                    <div class="profile-container">
                        <form id="profileForm">
                            <div class="form-group">
                                <label>工号</label>
                                <div class="static-text">${faculty.facultyId}</div>
                            </div>
                            <div class="form-group">
                                <label>姓名</label>
                                <div class="static-text">${faculty.facultyName}</div>
                            </div>
                            <div class="form-group">
                                <label>所属院系</label>
                                <div class="static-text">${faculty.departmentName}</div>
                            </div>
                            <div class="form-group">
                                <label>入职日期</label>
                                <div class="static-text">${faculty.hireDate}</div>
                            </div>
                            <div class="form-group">
                                <label for="phoneNumber">联系电话</label>
                                <input type="tel" id="phoneNumber" name="phoneNumber" value="${faculty.phoneNumber}"
                                    required>
                            </div>
                            <div class="form-group">
                                <label for="idNumber">身份证号</label>
                                <input type="text" id="idNumber" name="idNumber" value="${faculty.idNumber}" required>
                            </div>
                            <div class="form-group">
                                <label for="password">修改密码</label>
                                <input type="password" id="password" name="password" placeholder="留空则保持原密码不变">
                            </div>
                            <div class="button-group">
                                <button type="submit" class="btn-submit">保存修改</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/static/js/teacher.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/profile.js"></script>
        <script>
            // 确保contextPath正确设置
            console.log('contextPath:', contextPath);

            // 测试submitUpdate函数是否正确加载
            console.log('submitUpdate function loaded:', typeof submitUpdate === 'function');
        </script>
    </body>

    </html>