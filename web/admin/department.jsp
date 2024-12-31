<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 院系管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/department.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <!-- 引入侧边栏 -->
                <jsp:include page="components/sidebar.jsp" />

                <!-- 主要内容区域 -->
                <div class="main-content">
                    <div class="header">
                        <div class="page-title">院系管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加院系</button>
                                <button class="update-btn" onclick="updateDepartmentCount()">更新人数</button>
                                <button class="batch-delete-btn" onclick="batchDelete()">批量删除</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索院系..." onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="department-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                        <th>院系编号</th>
                                        <th>院系名称</th>
                                        <th>教职工人数</th>
                                        <th>院系管理员</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${departmentList}" var="dept">
                                        <tr>
                                            <td><input type="checkbox" name="deptIds" value="${dept.deptId}"></td>
                                            <td>${dept.deptId}</td>
                                            <td>${dept.deptName}</td>
                                            <td>${dept.numOfPeople}</td>
                                            <td>${dept.adminName}</td>
                                            <td>
                                                <a href="javascript:void(0)" onclick="showEditModal('${dept.deptId}')"
                                                    class="action-link">编辑</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteDepartment('${dept.deptId}')"
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

            <!-- 添加院系模态框 -->
            <div id="addDepartmentModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加院系</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addDepartmentForm">
                            <div class="form-group">
                                <label for="deptId">院系编号</label>
                                <input type="text" id="deptId" name="deptId" required>
                                <div class="error-message">请输入正确的院系编号(2-10位大写字母和数字)</div>
                            </div>
                            <div class="form-group">
                                <label for="deptName">院系名称</label>
                                <input type="text" id="deptName" name="deptName" required>
                                <div class="error-message">请输入院系名称(2-100个字符)</div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAddDepartment()">确定</button>
                    </div>
                </div>
            </div>

            <!-- 编辑院系模态框 -->
            <div id="editDepartmentModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>编辑院系</h2>
                        <span class="close" onclick="closeEditModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="editDepartmentForm">
                            <input type="hidden" id="editDeptId" name="deptId">
                            <div class="form-group">
                                <label for="editDeptName">院系名称</label>
                                <input type="text" id="editDeptName" name="deptName" required>
                                <div class="error-message">请输入院系名称(2-100个字符)</div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeEditModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitEditDepartment()">保存</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/department.js"></script>
        </body>

        </html>