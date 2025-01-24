<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>教职工管理系统 - 我的课表</title>
            <script>
                // 确保 contextPath 正确设置
                var contextPath = '${pageContext.request.contextPath}';
                console.log('Context Path initialized:', contextPath);

                // 添加错误处理
                window.onerror = function (msg, url, line) {
                    console.error('JavaScript error:', msg);
                    console.error('URL:', url);
                    console.error('Line:', line);
                    return false;
                };
            </script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/teacher.css">
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/schedule.css">
        </head>

        <body>
            <div class="admin-container">
                <jsp:include page="components/sidebar.jsp" />

                <div class="main-content">
                    <div class="header">
                        <div class="page-title">我的课表</div>
                        <div class="user-info">
                            欢迎，${sessionScope.faculty.facultyName}
                            <span class="department-name">${sessionScope.faculty.departmentName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                        </div>
                    </div>

                    <div class="content">
                        <div class="schedule-container">
                            <div class="schedule-header">
                                <div class="week-selector">
                                    <label for="weekSelect">教学周：</label>
                                    <select id="weekSelect"
                                        onchange="console.log('Native onchange event'); changeWeek(this.value);">
                                        <c:forEach begin="1" end="16" var="week">
                                            <option value="${week}" ${currentWeek==week ? 'selected' : '' }>
                                                第${week}周
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="schedule-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>节次/星期</th>
                                            <th>星期一</th>
                                            <th>星期二</th>
                                            <th>星期三</th>
                                            <th>星期四</th>
                                            <th>星期五</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="1" end="5" var="classTime">
                                            <tr>
                                                <td class="class-time">第${classTime}节</td>
                                                <c:forEach begin="1" end="5" var="weekDay">
                                                    <td class="schedule-cell">
                                                        <c:forEach items="${scheduleMap[weekDay][classTime]}"
                                                            var="schedule">
                                                            <div class="course-info">
                                                                <div class="course-name">${schedule.courseName}</div>
                                                                <div class="course-location">${schedule.location}</div>
                                                                <div class="course-class">${schedule.targetInfo}</div>
                                                            </div>
                                                        </c:forEach>
                                                    </td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="${pageContext.request.contextPath}/static/js/teacher.js"></script>
            <script src="${pageContext.request.contextPath}/static/js/schedule.js"></script>
            <script>
                // 页面加载完成后的初始化
                document.addEventListener('DOMContentLoaded', function () {
                    console.log('Page loaded');
                    console.log('Current Week:', ${ currentWeek });
                    console.log('Context Path:', contextPath);

                    // 手动初始化周选择器
                    const weekSelect = document.getElementById('weekSelect');
                    if (weekSelect) {
                        console.log('Week selector found');
                        weekSelect.addEventListener('change', function () {
                            console.log('Week changed to:', this.value);
                            window.location.href = contextPath + '/teacher/schedule?week=' + this.value;
                        });
                    } else {
                        console.error('Week selector not found');
                    }
                });
            </script>
        </body>

        </html>