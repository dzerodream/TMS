<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 离职员工管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/faculty.css">
        </head>

        <body>
            <div class="admin-container">
                <!-- 引入侧边栏 -->
                <jsp:include page="components/sidebar.jsp" />

                <!-- 主要内容区域 -->
                <div class="main-content">
                    <div class="header">
                        <div class="page-title">离职员工管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="batch-delete-btn" onclick="batchDelete()">批量删除</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索工号、姓名、院系..."
                                        onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="faculty-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                        <th>姓名</th>
                                        <th>工号</th>
                                        <th>联系手机号</th>
                                        <th>原所属院系</th>
                                        <th>离职日期</th>
                                        <th>已离职天数</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${retiredList}" var="faculty">
                                        <tr>
                                            <td><input type="checkbox" name="facultyIds" value="${faculty.facultyId}">
                                            </td>
                                            <td>${faculty.facultyName}</td>
                                            <td>${faculty.facultyId}</td>
                                            <td>${faculty.phoneNumber}</td>
                                            <td>${faculty.departmentName}</td>
                                            <td>${faculty.leaveDate}</td>
                                            <td>${faculty.leaveDays}天</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="restoreFaculty(${faculty.facultyId})"
                                                    class="action-link restore">恢复</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteRetiredFaculty(${faculty.facultyId})"
                                                    class="action-link delete">删除</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/retired.js"></script>
        </body>

        </html>