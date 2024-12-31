<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 学生管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/student.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <!-- 引入侧边栏 -->
                <jsp:include page="components/sidebar.jsp" />

                <!-- 主要内容区域 -->
                <div class="main-content">
                    <div class="header">
                        <div class="page-title">学生管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加学生</button>
                                <button class="batch-delete-btn" onclick="batchDelete()">批量删除</button>
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
                                        <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                        <th>姓名</th>
                                        <th>学号</th>
                                        <th>性别</th>
                                        <th>状态(毕业/在校)</th>
                                        <th>所在院系</th>
                                        <th>所在班级</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${studentList}" var="student">
                                        <tr>
                                            <td><input type="checkbox" name="studentIds" value="${student.studentId}">
                                            </td>
                                            <td>${student.studentName}</td>
                                            <td>${student.studentId}</td>
                                            <td>${student.gender}</td>
                                            <td>${student.status == 1 ? '在校' : '毕业'}</td>
                                            <td>${student.departmentName}</td>
                                            <td>${student.className}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="showDetailModal('${student.studentId}')"
                                                    class="action-link">详情</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="toggleStudentStatus('${student.studentId}', ${student.status})"
                                                    class="action-link ${student.status == 1 ? 'graduate' : 'enroll'}">${student.status
                                                    == 1 ? '设为毕业' : '设为在校'}</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteStudent('${student.studentId}')"
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

            <!-- 添加学生模态框 -->
            <div id="addStudentModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加学生</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addStudentForm">
                            <div class="form-group">
                                <label for="studentId">学号</label>
                                <input type="text" id="studentId" name="studentId" required>
                            </div>
                            <div class="form-group">
                                <label for="studentName">姓名</label>
                                <input type="text" id="studentName" name="studentName" required>
                            </div>
                            <div class="form-group">
                                <label for="gender">性别</label>
                                <select id="gender" name="gender" required>
                                    <option value="">请选择性别</option>
                                    <option value="M">男</option>
                                    <option value="F">女</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="idNumber">身份证号</label>
                                <input type="text" id="idNumber" name="idNumber" required>
                            </div>
                            <div class="form-group">
                                <label for="phoneNumber">联系电话</label>
                                <input type="tel" id="phoneNumber" name="phoneNumber" required>
                            </div>
                            <div class="form-group">
                                <label for="departmentId">所在院系</label>
                                <select id="departmentId" name="departmentId" required>
                                    <option value="">请选择院系</option>
                                    <c:forEach items="${departmentList}" var="dept">
                                        <option value="${dept.deptId}">${dept.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="classId">所在班级</label>
                                <select id="classId" name="classId" required>
                                    <option value="">请选择班级</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAddStudent()">确定</button>
                    </div>
                </div>
            </div>

            <!-- 学生详情模态框 -->
            <div id="detailStudentModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>学生详情</h2>
                        <span class="close" onclick="closeDetailModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="detailStudentForm">
                            <div class="form-group">
                                <label for="detail_studentId">学号</label>
                                <input type="text" id="detail_studentId" name="studentId" readonly>
                            </div>
                            <div class="form-group">
                                <label for="detail_studentName">姓名</label>
                                <input type="text" id="detail_studentName" name="studentName" required>
                            </div>
                            <div class="form-group">
                                <label for="detail_gender">性别</label>
                                <select id="detail_gender" name="gender" required>
                                    <option value="M">男</option>
                                    <option value="F">女</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="detail_idNumber">身份证号</label>
                                <input type="text" id="detail_idNumber" name="idNumber" required>
                            </div>
                            <div class="form-group">
                                <label for="detail_phoneNumber">联系电话</label>
                                <input type="tel" id="detail_phoneNumber" name="phoneNumber" required>
                            </div>
                            <div class="form-group">
                                <label for="detail_departmentId">所在院系</label>
                                <select id="detail_departmentId" name="departmentId" required>
                                    <c:forEach items="${departmentList}" var="dept">
                                        <option value="${dept.deptId}">${dept.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="detail_classId">所在班级</label>
                                <select id="detail_classId" name="classId" required>
                                    <option value="">请选择班级</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeDetailModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitUpdateStudent()">保存修改</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/student.js"></script>
        </body>

        </html>