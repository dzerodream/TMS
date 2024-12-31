<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 课程管理</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/course.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">课程管理</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加课程</button>
                                <button class="batch-delete-btn" onclick="batchDelete()">批量删除</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索课程编号/名称/院系..."
                                        onkeyup="handleSearch()" autocomplete="off">
                                    <i class="search-icon" onclick="handleSearch()">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="course-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                        <th>课程编号</th>
                                        <th>课程名称</th>
                                        <th>所属院系</th>
                                        <th>学分</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${courseList}" var="course">
                                        <tr>
                                            <td><input type="checkbox" class="course-checkbox"
                                                    value="${course.courseId}"></td>
                                            <td>${course.courseId}</td>
                                            <td>${course.courseName}</td>
                                            <td>${course.departmentName}</td>
                                            <td>${course.credits}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="showEditModal('${course.courseId}')"
                                                    class="action-link">编辑</a>
                                                <span class="separator">|</span>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteCourse('${course.courseId}')"
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

            <!-- 添加课程模态框 -->
            <div id="addCourseModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加课程</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addCourseForm">
                            <div class="form-group">
                                <label for="courseId">课程编号</label>
                                <input type="text" id="courseId" name="courseId" required>
                            </div>
                            <div class="form-group">
                                <label for="courseName">课程名称</label>
                                <input type="text" id="courseName" name="courseName" required>
                            </div>
                            <div class="form-group">
                                <label for="deptId">所属院系</label>
                                <select id="deptId" name="deptId" required>
                                    <option value="">请选择院系</option>
                                    <c:forEach items="${departmentList}" var="dept">
                                        <option value="${dept.deptId}">${dept.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="credits">学分</label>
                                <input type="number" id="credits" name="credits" step="0.5" min="0" required>
                            </div>
                            <div class="form-group">
                                <label for="totalHours">总学时</label>
                                <input type="number" id="totalHours" name="totalHours" min="0" required>
                            </div>
                            <div class="form-group">
                                <label for="courseNature">课程性质</label>
                                <select id="courseNature" name="courseNature" required>
                                    <option value="">请选择课程性质</option>
                                    <option value="必修">必修</option>
                                    <option value="选修">选修</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="assessmentMethod">考核方式</label>
                                <select id="assessmentMethod" name="assessmentMethod" required>
                                    <option value="">请选择考核方式</option>
                                    <option value="笔试">笔试</option>
                                    <option value="实验">实验</option>
                                    <option value="论文">论文</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAddCourse()">确定</button>
                    </div>
                </div>
            </div>

            <!-- 编辑课程模态框 -->
            <div id="editCourseModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>编辑课程</h2>
                        <span class="close" onclick="closeEditModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="editCourseForm">
                            <input type="hidden" id="edit_courseId" name="courseId">
                            <div class="form-group">
                                <label for="edit_courseName">课程名称</label>
                                <input type="text" id="edit_courseName" name="courseName" required>
                            </div>
                            <div class="form-group">
                                <label for="edit_deptId">所属院系</label>
                                <select id="edit_deptId" name="deptId" required>
                                    <option value="">请选择院系</option>
                                    <c:forEach items="${departmentList}" var="dept">
                                        <option value="${dept.deptId}">${dept.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="edit_credits">学分</label>
                                <input type="number" id="edit_credits" name="credits" step="0.5" min="0" required>
                            </div>
                            <div class="form-group">
                                <label for="edit_totalHours">总学时</label>
                                <input type="number" id="edit_totalHours" name="totalHours" min="0" required>
                            </div>
                            <div class="form-group">
                                <label for="edit_courseNature">课程性质</label>
                                <select id="edit_courseNature" name="courseNature" required>
                                    <option value="">请选择课程性质</option>
                                    <option value="必修">必修</option>
                                    <option value="选修">选修</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="edit_assessmentMethod">考核方式</label>
                                <select id="edit_assessmentMethod" name="assessmentMethod" required>
                                    <option value="">请选择考核方式</option>
                                    <option value="笔试">笔试</option>
                                    <option value="实验">实验</option>
                                    <option value="论文">论文</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeEditModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitEditCourse()">保存</button>
                    </div>
                </div>
            </div>

            <!-- 添加无搜索结果提示 -->
            <div class="no-result" id="noResult">
                未找到匹配的课程
            </div>

            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/course.js"></script>
        </body>

        </html>