<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 教师管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/faculty.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">教师管理</div>
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
                                    <input type="text" id="searchInput" placeholder="搜索工号、姓名、手机号..."
                                        onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="faculty-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>姓名</th>
                                        <th>工号</th>
                                        <th>联系手机号</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${facultyList}" var="faculty">
                                        <tr>
                                            <td>${faculty.facultyName}</td>
                                            <td>${faculty.facultyId}</td>
                                            <td>${faculty.phoneNumber}</td>
                                            <td>
                                                <a href="javascript:void(0)" onclick="showDetails(${faculty.facultyId})"
                                                    class="action-link">详情</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)" onclick="handleLeave(${faculty.facultyId})"
                                                    class="action-link delete">离职</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/dept_faculty.js"></script>
        </body>

        </html>