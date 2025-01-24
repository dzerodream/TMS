<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 教师详情</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">教师详情</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="detail-container">
                            <form id="facultyDetailForm">
                                <input type="hidden" id="facultyId" name="facultyId" value="${faculty.facultyId}">
                                <div class="form-group">
                                    <label for="facultyName">姓名</label>
                                    <input type="text" id="facultyName" name="facultyName"
                                        value="${faculty.facultyName}" required>
                                </div>
                                <div class="form-group">
                                    <label for="phoneNumber">联系手机号</label>
                                    <input type="tel" id="phoneNumber" name="phoneNumber" value="${faculty.phoneNumber}"
                                        required>
                                </div>
                                <div class="form-group">
                                    <label for="idNumber">身份证号</label>
                                    <input type="text" id="idNumber" name="idNumber" value="${faculty.idNumber}"
                                        required>
                                </div>
                                <div class="form-group">
                                    <label for="password">重置密码</label>
                                    <input type="password" id="password" name="password">
                                    <div class="password-hint">留空则保持原密码不变</div>
                                </div>
                                <div class="form-group">
                                    <label>入职日期</label>
                                    <div class="static-text">${faculty.hireDate}</div>
                                </div>
                                <div class="form-group">
                                    <label>工号</label>
                                    <div class="static-text">${faculty.facultyId}</div>
                                </div>
                                <div class="form-group">
                                    <label>所属院系</label>
                                    <div class="static-text">${faculty.departmentName}</div>
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
            <script src="${pageContext.request.contextPath}/static/js/faculty_detail.js"></script>
        </body>

        </html>