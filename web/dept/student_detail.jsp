<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 学生详情</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css"
                href="${pageContext.request.contextPath}/static/css/student_detail.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">学生详情</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="detail-container">
                            <form id="studentDetailForm">
                                <input type="hidden" id="studentId" name="studentId" value="${student.studentId}">
                                <div class="form-group">
                                    <label for="studentName">姓名</label>
                                    <input type="text" id="studentName" name="studentName"
                                        value="${student.studentName}" required>
                                </div>
                                <div class="form-group">
                                    <label for="gender">性别</label>
                                    <select id="gender" name="gender" required>
                                        <option value="M" ${student.gender=='M' ? 'selected' : '' }>男</option>
                                        <option value="F" ${student.gender=='F' ? 'selected' : '' }>女</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="phoneNumber">联系电话</label>
                                    <input type="tel" id="phoneNumber" name="phoneNumber" value="${student.phoneNumber}"
                                        required>
                                </div>
                                <div class="form-group">
                                    <label for="idNumber">身份证号</label>
                                    <input type="text" id="idNumber" name="idNumber" value="${student.idNumber}"
                                        required>
                                </div>
                                <div class="form-group">
                                    <label for="classId">班级</label>
                                    <select id="classId" name="classId" required>
                                        <c:forEach items="${classList}" var="classInfo">
                                            <option value="${classInfo.classId}" ${classInfo.classId==student.classId
                                                ? 'selected' : '' }>
                                                ${classInfo.className}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>学号</label>
                                    <div class="static-text">${student.studentId}</div>
                                </div>
                                <div class="form-group">
                                    <label>入学年份</label>
                                    <div class="static-text">${student.enrollmentYear}</div>
                                </div>
                                <div class="form-group">
                                    <label>所属院系</label>
                                    <div class="static-text">${student.departmentName}</div>
                                </div>
                                <div class="form-group">
                                    <label>状态</label>
                                    <div class="static-text">${student.status == 1 ? '在校' : '已毕业'}</div>
                                </div>
                                <div class="button-group">
                                    <button type="button" class="btn-cancel" onclick="history.back()">返回</button>
                                    <button type="button" class="btn-submit" onclick="submitUpdate()">保存修改</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/student_detail.js"></script>
        </body>

        </html>