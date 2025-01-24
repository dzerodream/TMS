<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>教职工管理系统 - 院系管理面板</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
        <style>
            .stat-cards {
                display: flex;
                gap: 20px;
                margin-bottom: 30px;
            }

            .stat-card {
                flex: 1;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .stat-icon {
                font-size: 24px;
                width: 40px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 8px;
            }

            .stat-info {
                flex: 1;
            }

            .stat-value {
                font-size: 24px;
                font-weight: bold;
                color: #1890ff;
                margin-bottom: 5px;
            }

            .stat-label {
                font-size: 14px;
                color: #666;
            }

            .quick-actions {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 20px;
            }

            .action-card {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                text-align: center;
                cursor: pointer;
                transition: transform 0.2s;
            }

            .action-card:hover {
                transform: translateY(-5px);
            }

            .action-icon {
                font-size: 24px;
                margin-bottom: 10px;
            }

            .action-title {
                font-size: 16px;
                color: #333;
                margin-bottom: 5px;
            }
        </style>
    </head>

    <body>
        <div class="admin-container">
            <jsp:include page="components/sidebar.jsp" />

            <div class="main-content">
                <div class="header">
                    <div class="page-title">院系管理面板</div>
                    <div class="user-info">
                        欢迎，${sessionScope.faculty.facultyName}
                        <span class="department-name">${sessionScope.faculty.departmentName}</span>
                        <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                    </div>
                </div>
                <div class="content">
                    <div class="stat-cards">
                        <div class="stat-card">
                            <div class="stat-icon">👥</div>
                            <div class="stat-info">
                                <div class="stat-value">${facultyCount}</div>
                                <div class="stat-label">在职教职工</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">🏛️</div>
                            <div class="stat-info">
                                <div class="stat-value">${departmentCount}</div>
                                <div class="stat-label">院系数量</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">👨‍🎓</div>
                            <div class="stat-info">
                                <div class="stat-value">${studentCount}</div>
                                <div class="stat-label">在校学生</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">📚</div>
                            <div class="stat-info">
                                <div class="stat-value">${courseCount}</div>
                                <div class="stat-label">开设课程</div>
                            </div>
                        </div>
                    </div>

                    <div class="section-title">快捷操作</div>
                    <div class="quick-actions">
                        <a href="${pageContext.request.contextPath}/dept/faculty" class="action-card">
                            <div class="action-icon">👨‍🏫</div>
                            <div class="action-title">教职工管理</div>
                        </a>
                        <a href="${pageContext.request.contextPath}/dept/student" class="action-card">
                            <div class="action-icon">👨‍🎓</div>
                            <div class="action-title">学生管理</div>
                        </a>
                        <a href="${pageContext.request.contextPath}/dept/course" class="action-card">
                            <div class="action-icon">📚</div>
                            <div class="action-title">课程管理</div>
                        </a>
                        <a href="${pageContext.request.contextPath}/dept/salary/log" class="action-card">
                            <div class="action-icon">💰</div>
                            <div class="action-title">薪酬查看</div>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
    </body>

    </html>