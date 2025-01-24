<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 学生管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/student.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">学生管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加学生</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索姓名、学号..."
                                        onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="student-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>姓名</th>
                                        <th>学号</th>
                                        <th>性别</th>
                                        <th>状态(毕业/在校)</th>
                                        <th>所在班级</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${studentList}" var="student">
                                        <tr>
                                            <td>${student.studentName}</td>
                                            <td>${student.studentId}</td>
                                            <td>${student.gender == 'M' ? '男' : '女'}</td>
                                            <td>${student.status == 1 ? '在校' : '已毕业'}</td>
                                            <td>${student.className}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="showDetails('${student.studentId}')"
                                                    class="action-link">详情</a>
                                                <c:if test="${student.status == 1}">
                                                    <span class="separator">|</span>
                                                    <a href="javascript:void(0)"
                                                        onclick="handleGraduate('${student.studentId}')"
                                                        class="action-link graduate">设为毕业</a>
                                                </c:if>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="handleDelete('${student.studentId}')"
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

            <div id="addModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加学生</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addStudentForm">
                            <div class="form-group">
                                <label for="studentName">姓名</label>
                                <input type="text" id="studentName" name="studentName" required>
                            </div>
                            <div class="form-group">
                                <label for="gender">性别</label>
                                <select id="gender" name="gender" required>
                                    <option value="M">男</option>
                                    <option value="F">女</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="enrollmentYear">入学年份</label>
                                <input type="number" id="enrollmentYear" name="enrollmentYear" value="${currentYear}"
                                    min="2000" max="2100" required>
                            </div>
                            <div class="form-group">
                                <label for="classId">班级</label>
                                <select id="classId" name="classId" required>
                                    <c:forEach items="${classList}" var="classInfo">
                                        <option value="${classInfo.classId}">${classInfo.className}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="phoneNumber">联系电话</label>
                                <input type="tel" id="phoneNumber" name="phoneNumber" required>
                            </div>
                            <div class="form-group">
                                <label for="idNumber">身份证号</label>
                                <input type="text" id="idNumber" name="idNumber" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAdd()">添加</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/dept_student.js"></script>
        </body>

        </html>