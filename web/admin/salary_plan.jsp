<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 薪酬计划</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/salary_plan.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">薪酬计划</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="salary-plan-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>角色</th>
                                        <th>基本工资</th>
                                        <th>当前人数</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${salaryPlans}" var="plan">
                                        <tr>
                                            <td>${plan.roleName}</td>
                                            <td>
                                                <span class="salary-amount">${plan.baseSalary}</span>
                                                <span class="currency">元</span>
                                            </td>
                                            <td>${plan.facultyCount}人</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="showEditModal('${plan.roleId}', '${plan.roleName}', '${plan.baseSalary}')"
                                                    class="action-link">修改工资</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 编辑工资模态框 -->
            <div id="editSalaryModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>修改基本工资</h2>
                        <span class="close" onclick="closeEditModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="editSalaryForm">
                            <input type="hidden" id="roleId" name="roleId">
                            <div class="form-group">
                                <label for="roleName">角色</label>
                                <input type="text" id="roleName" readonly>
                            </div>
                            <div class="form-group">
                                <label for="baseSalary">基本工资</label>
                                <input type="number" id="baseSalary" name="baseSalary" step="0.01" min="0" required>
                                <span class="currency">元</span>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeEditModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitEditSalary()">保存</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/salary_plan.js"></script>
        </body>

        </html>