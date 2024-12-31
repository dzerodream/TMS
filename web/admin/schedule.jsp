<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 课程安排</title>
            <script>var contextPath = '${pageContext.request.contextPath}';</script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/schedule.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/modal.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">课程安排</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>
                    <div class="content">
                        <div class="toolbar">
                            <div class="toolbar-left">
                                <button class="add-btn" onclick="showAddModal()">+ 添加课程安排</button>
                                <button class="batch-delete-btn" onclick="batchDelete()">批量删除</button>
                            </div>
                            <div class="toolbar-right">
                                <div class="search-box">
                                    <input type="text" id="searchInput" placeholder="搜索课程名称、教师..."
                                        onkeyup="handleSearch()">
                                    <i class="search-icon">🔍</i>
                                </div>
                            </div>
                        </div>
                        <div class="schedule-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                        <th>课程名称</th>
                                        <th>上课时间</th>
                                        <th>上课地点</th>
                                        <th>任课教师</th>
                                        <th>上课对象</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${scheduleList}" var="schedule">
                                        <tr>
                                            <td><input type="checkbox" class="schedule-checkbox"
                                                    value="${schedule.scheduleId}"></td>
                                            <td>${schedule.courseName}</td>
                                            <td>第${schedule.startWeek}-${schedule.endWeek}周
                                                星期${schedule.weekDay} ${schedule.classTime}节</td>
                                            <td>${schedule.location}</td>
                                            <td>${schedule.facultyName}</td>
                                            <td>${schedule.targetInfo}</td>
                                            <td>
                                                <a href="javascript:void(0)"
                                                    onclick="deleteSchedule(${schedule.scheduleId})"
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

            <!-- 添加课程安排模态框 -->
            <div id="addScheduleModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>添加课程安排</h2>
                        <span class="close" onclick="closeAddModal()">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="addScheduleForm">
                            <div class="form-group">
                                <label for="courseId">课程</label>
                                <select id="courseId" name="courseId" required>
                                    <option value="">请选择课程</option>
                                    <c:forEach items="${courseList}" var="course">
                                        <option value="${course.courseId}">${course.courseName}
                                            (${course.departmentName})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="facultyId">任课教师</label>
                                <select id="facultyId" name="facultyId" required>
                                    <option value="">请选择教师</option>
                                    <c:forEach items="${facultyList}" var="faculty">
                                        <option value="${faculty.facultyId}">${faculty.facultyName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>上课时间</label>
                                <div class="time-inputs">
                                    <input type="number" id="startWeek" name="startWeek" min="1" max="20" required
                                        placeholder="起始周">
                                    <span>-</span>
                                    <input type="number" id="endWeek" name="endWeek" min="1" max="20" required
                                        placeholder="结束周">
                                    <select id="weekDay" name="weekDay" required>
                                        <option value="">星期</option>
                                        <option value="1">星期一</option>
                                        <option value="2">星期二</option>
                                        <option value="3">星期三</option>
                                        <option value="4">星期四</option>
                                        <option value="5">星期五</option>
                                    </select>
                                    <input type="text" id="classTime" name="classTime" required placeholder="第几节课">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="location">上课地点</label>
                                <input type="text" id="location" name="location" required>
                            </div>
                            <div class="form-group">
                                <label>上课对象</label>
                                <div class="target-selection">
                                    <select id="targetType" onchange="handleTargetTypeChange()">
                                        <option value="class">按班级</option>
                                        <option value="student">按学生</option>
                                    </select>
                                    <div id="classSelection">
                                        <select multiple id="classIds">
                                            <c:forEach items="${classList}" var="classItem">
                                                <option value="${classItem.classId}">${classItem.className}
                                                    (${classItem.departmentName})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div id="studentSelection" style="display:none">
                                        <select multiple id="studentIds">
                                            <c:forEach items="${studentList}" var="student">
                                                <option value="${student.studentId}">${student.studentName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                        <button type="button" class="btn-submit" onclick="submitAddSchedule()">确定</button>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/schedule.js"></script>
        </body>

        </html>