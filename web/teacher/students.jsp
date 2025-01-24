<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 学生名单</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/teacher.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/students.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">学生名单</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>

                    <div class="content">
                        <div class="students-container">
                            <div class="toolbar">
                                <div class="course-selector">
                                    <label for="courseSelect">选择课程：</label>
                                    <select id="courseSelect" onchange="changeCourse(this.value)">
                                        <c:forEach items="${courses}" var="course">
                                            <option value="${course.courseId}" ${currentCourse==course.courseId
                                                ? 'selected' : '' }>
                                                ${course.courseName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索学生..." oninput="handleSearch()">
                                </div>
                            </div>

                            <div class="students-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>学号</th>
                                            <th>姓名</th>
                                            <th>班级</th>
                                            <th>联系电话</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${students}" var="student">
                                            <tr>
                                                <td>${student.studentId}</td>
                                                <td>${student.studentName}</td>
                                                <td>${student.className}</td>
                                                <td>${student.phoneNumber}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/teacher.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/students.js"></script>
        </body>

        </html>