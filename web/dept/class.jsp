<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 班级管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/class.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">班级管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加班级</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索班级..." onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="class-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>班级名称</th>
                                        <th>班级人数</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${classList}" var="classInfo">
                                        <tr>
                                            <td>${classInfo.className}</td>
                                            <td>${classInfo.studentCount}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteClass('${classInfo.classId}')"
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

            <!-- 添加班级模态框 -->
            <div id="addClassModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加班级</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addClassForm">
                            <div class="form-group">
                                <label for="classId">班级编号</label>
                                <input type="text" id="classId" name="classId" required>
                            </div>
                            <div class="form-group">
                                <label for="className">班级名称</label>
                                <input type="text" id="className" name="className" required>
                            </div>
                            <input type="hidden" id="departmentId" name="departmentId"
                                value="${sessionScope.faculty.departmentId}">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAddClass()">确定</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/dept_class.js"></script>
        </body>

        </html>