<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>教职工管理系统 - 薪资查看</title>
                <script>var contextPath = '${pageContext.request.contextPath}';</script>
                <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
                <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dept.css">
                <link rel="stylesheet" type="text/css"
                    href="${pageContext.request.contextPath}/static/css/salary_log.css">
            </head>

            <body>
                <div class="admin-container">
                    <jsp:include page="components/sidebar.jsp" />

                    <div class="main-content">
                        <div class="header">
                            <div class="page-title">薪资查看</div>
                            <div class="user-info">
                                欢迎，${sessionScope.faculty.facultyName}
                                <span class="department-name">${sessionScope.faculty.departmentName}</span>
                                <a href="${pageContext.request.contextPath}/logout" class="logout">退出登录</a>
                            </div>
                        </div>
                        <div class="content">
                            <div class="toolbar">
                                <div class="toolbar-left">
                                    <div class="month-selector">
                                        <label for="monthSelect">选择月份：</label>
                                        <select id="monthSelect" onchange="changeMonth(this.value)">
                                            <option value="3" ${currentMonth==3 ? 'selected' : '' }>3月</option>
                                            <option value="4" ${currentMonth==4 ? 'selected' : '' }>4月</option>
                                            <option value="5" ${currentMonth==5 ? 'selected' : '' }>5月</option>
                                            <option value="6" ${currentMonth==6 ? 'selected' : '' }>6月</option>
                                            <option value="9" ${currentMonth==9 ? 'selected' : '' }>9月</option>
                                            <option value="10" ${currentMonth==10 ? 'selected' : '' }>10月</option>
                                            <option value="11" ${currentMonth==11 ? 'selected' : '' }>11月</option>
                                            <option value="12" ${currentMonth==12 ? 'selected' : '' }>12月</option>
                                            <option value="1" ${currentMonth==1 ? 'selected' : '' }>1月</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="toolbar-right">
                                    <div class="search-box">
                                        <input type="text" id="searchInput" placeholder="搜索教师姓名..."
                                            onkeyup="handleSearch()">
                                        <i class="search-icon">🔍</i>
                                    </div>
                                </div>
                            </div>
                            <div class="salary-log-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>教师姓名</th>
                                            <th>基本工资</th>
                                            <th>课时数</th>
                                            <th>课时费</th>
                                            <th>总薪资</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${salaryLogs}" var="log">
                                            <tr>
                                                <td>${log.facultyName}</td>
                                                <td>
                                                    <fmt:formatNumber value="${log.baseSalary}" pattern="#,##0.00" />
                                                </td>
                                                <td>${log.totalHours}</td>
                                                <td>
                                                    <fmt:formatNumber value="${log.hourlyPay}" pattern="#,##0.00" />
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${log.totalSalary}" pattern="#,##0.00" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="${pageContext.request.contextPath}/static/js/dept.js"></script>
                <script src="${pageContext.request.contextPath}/static/js/dept_salary_log.js"></script>
            </body>

            </html>