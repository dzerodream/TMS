<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>教职工管理系统 - 管理面板</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
    </head>

    <body>
        <div class="admin-container">
            <!-- 侧边栏 -->
            <div class="sidebar">
                <div class="logo">教职工管理系统</div>
                <nav class="menu">
                    <div class="menu-group">
                        <div class="menu-title">教职工管理</div>
                        <ul class="menu-items">
                            <li><a href="${pageContext.request.contextPath}/admin/faculty">员工管理</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/department">院系管理</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/retired">已离职员工管理</a></li>
                        </ul>
                    </div>
                    <div class="menu-group">
                        <div class="menu-title">学生管理</div>
                        <ul class="menu-items">
                            <li><a href="${pageContext.request.contextPath}/admin/student">学生管理</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/class">班级管理</a></li>
                        </ul>
                    </div>
                    <div class="menu-group">
                        <div class="menu-title">课程管理</div>
                        <ul class="menu-items">
                            <li><a href="${pageContext.request.contextPath}/admin/course">课程管理</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/schedule">课程安排</a></li>
                        </ul>
                    </div>
                    <div class="menu-group">
                        <div class="menu-title">薪酬管理</div>
                        <ul class="menu-items">
                            <li><a href="${pageContext.request.contextPath}/admin/salary/plan">薪酬计划</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/salary/log">薪酬日志</a></li>
                        </ul>
                    </div>
                </nav>
            </div>

            <!-- 主要内容区域 -->
            <div class="main-content">
                <div class="header">
                    <div class="user-info">
                        欢迎，${sessionScope.faculty.facultyName}
                        <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                    </div>
                </div>
                <div class="content">
                    <!-- 这里是具体页面的内容 -->
                    <h1>欢迎使用教职工管理系统</h1>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
    </body>

    </html>