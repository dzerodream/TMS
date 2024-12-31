<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 员工管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <script src="https://cdn.jsdelivr.net/npm/gson@0.1.5/gson.min.js"></script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/faculty.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <!-- 引入侧边栏 -->
                <jsp:include page="components/sidebar.jsp" />

                <!-- 主要内容区域 -->
                <div class="main-content">
                    <div class="header">
                        <div class="page-title">员工管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加教职工</button>
                                <button class="batch-delete-btn" onclick="batchDelete()">批量离职</button>
                            </div>
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
                                        <th><input type="checkbox" id="selectAll"></th>
                                        <th>姓名</th>
                                        <th>工号</th>
                                        <th>联系手机号</th>
                                        <th>院系</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${facultyList}" var="faculty">
                                        <tr>
                                            <td><input type="checkbox" name="facultyIds" value="${faculty.facultyId}">
                                            </td>
                                            <td>
                                                <div class="faculty-name">
                                                    <c:if test="${faculty.roleId == 1}">
                                                        <span class="admin-tag">超级管理员</span>
                                                    </c:if>
                                                    ${faculty.facultyName}
                                                </div>
                                            </td>
                                            <td>${faculty.facultyId}</td>
                                            <td>${faculty.phoneNumber}</td>
                                            <td>${faculty.departmentName}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="showEditModal(${faculty.facultyId})"
                                                    class="action-link">详情</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteFaculty(${faculty.facultyId})"
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
            <jsp:include page="components/addFacultyModal.jsp" />
            <jsp:include page="components/editFacultyModal.jsp" />
            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/faculty.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/addFaculty.js"></script>
        </body>

        </html>