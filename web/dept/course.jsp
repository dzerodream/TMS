<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 课程信息</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/course.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">课程信息</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索课程编号/名称..."
                                        onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="course-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>课程编号</th>
                                        <th>课程名称</th>
                                        <th>学分</th>
                                        <th>总学时</th>
                                        <th>课程性质</th>
                                        <th>考核方式</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${courseList}" var="course">
                                        <tr>
                                            <td>${course.courseId}</td>
                                            <td>${course.courseName}</td>
                                            <td>${course.credits}</td>
                                            <td>${course.totalHours}</td>
                                            <td>${course.courseNature}</td>
                                            <td>${course.assessmentMethod}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/dept_course.js"></script>
        </body>

        </html>