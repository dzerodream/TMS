<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 个人中心</title>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/teacher.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">个人中心</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>

                    <div class="content">
                        <div class="stat-cards">
                            <div class="stat-card">
                                <div class="stat-icon">📚</div>
                                <div class="stat-info">
                                    <div class="stat-value">${teachingCourses}</div>
                                    <div class="stat-label">本学期课程</div>
                                </div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-icon">👨‍🎓</div>
                                <div class="stat-info">
                                    <div class="stat-value">${totalStudents}</div>
                                    <div class="stat-label">授课学生</div>
                                </div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-icon">⏰</div>
                                <div class="stat-info">
                                    <div class="stat-value">${weeklyHours}</div>
                                    <div class="stat-label">周课时数</div>
                                </div>
                            </div>
                        </div>

                        <div class="section-title">近期课程</div>
                        <div class="recent-schedule">
                            <table>
                                <thead>
                                    <tr>
                                        <th>课程名称</th>
                                        <th>上课时间</th>
                                        <th>上课地点</th>
                                        <th>上课班级</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentSchedules}" var="schedule">
                                        <tr>
                                            <td>${schedule.courseName}</td>
                                            <td>第${schedule.startWeek}-${schedule.endWeek}周
                                                星期${schedule.weekDay} ${schedule.classTime}</td>
                                            <td>${schedule.location}</td>
                                            <td>${schedule.targetInfo}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>