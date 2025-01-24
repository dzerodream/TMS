<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>教职工管理系统 - 管理面板</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dashboard.css">
    </head>

    <body>
        <div class="admin-container">
            <!-- 引入侧边栏 -->
            <jsp:include page="components/sidebar.jsp" />

            <!-- 主要内容区域 -->
            <div class="main-content">
                <div class="header">
                    <div class="page-title">管理控制台</div>
                    <div class="user-info">
                        欢迎，${sessionScope.faculty.facultyName}
                        <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                    </div>
                </div>
                <div class="content">
                    <!-- 统计卡片区域 -->
                    <div class="stat-cards">
                        <div class="stat-card faculty">
                            <div class="stat-icon">👥</div>
                            <div class="stat-info">
                                <div class="stat-value">${stats.facultyCount}</div>
                                <div class="stat-label">在职教职工</div>
                            </div>
                        </div>
                        <div class="stat-card department">
                            <div class="stat-icon">🏢</div>
                            <div class="stat-info">
                                <div class="stat-value">${stats.departmentCount}</div>
                                <div class="stat-label">院系数量</div>
                            </div>
                        </div>
                        <div class="stat-card student">
                            <div class="stat-icon">👨‍🎓</div>
                            <div class="stat-info">
                                <div class="stat-value">${stats.studentCount}</div>
                                <div class="stat-label">在校学生</div>
                            </div>
                        </div>
                        <div class="stat-card course">
                            <div class="stat-icon">📚</div>
                            <div class="stat-info">
                                <div class="stat-value">${stats.courseCount}</div>
                                <div class="stat-label">开设课程</div>
                            </div>
                        </div>
                    </div>

                    <!-- 快捷操作区域 -->
                    <div class="quick-actions">
                        <h2>快捷操作</h2>
                        <div class="action-grid">
                            <a href="${pageContext.request.contextPath}/admin/faculty" class="action-card">
                                <div class="action-icon">👤</div>
                                <div class="action-title">添加教职工</div>
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/department" class="action-card">
                                <div class="action-icon">🏛️</div>
                                <div class="action-title">管理院系</div>
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/course" class="action-card">
                                <div class="action-icon">📖</div>
                                <div class="action-title">课程管理</div>
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/salary/plan" class="action-card">
                                <div class="action-icon">💰</div>
                                <div class="action-title">薪酬设置</div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
    </body>

    </html>