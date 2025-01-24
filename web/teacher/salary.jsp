<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>教职工管理系统 - 薪资查看</title>
                <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css">
                <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/teacher.css">
                <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/salary.css">
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
                                <div class="month-selector">
                                    <label for="monthSelect">选择月份：</label>
                                    <select id="monthSelect" onchange="changeMonth(this.value)">
                                        <option value="1" ${currentMonth==1 ? 'selected' : '' }>第一学期 - 1月</option>
                                        <option value="3" ${currentMonth==3 ? 'selected' : '' }>第二学期 - 3月</option>
                                        <option value="4" ${currentMonth==4 ? 'selected' : '' }>第二学期 - 4月</option>
                                        <option value="5" ${currentMonth==5 ? 'selected' : '' }>第二学期 - 5月</option>
                                        <option value="6" ${currentMonth==6 ? 'selected' : '' }>第二学期 - 6月</option>
                                        <option value="9" ${currentMonth==9 ? 'selected' : '' }>第一学期 - 9月</option>
                                        <option value="10" ${currentMonth==10 ? 'selected' : '' }>第一学期 - 10月</option>
                                        <option value="11" ${currentMonth==11 ? 'selected' : '' }>第一学期 - 11月</option>
                                        <option value="12" ${currentMonth==12 ? 'selected' : '' }>第一学期 - 12月</option>
                                    </select>
                                </div>
                            </div>

                            <div class="salary-details">
                                <div class="salary-card">
                                    <div class="salary-header">
                                        <h3>薪资明细</h3>
                                        <div class="month-tag">${currentMonth}月</div>
                                    </div>
                                    <div class="salary-body">
                                        <div class="salary-item">
                                            <span class="item-label">基本工资</span>
                                            <span class="item-value">
                                                ￥
                                                <fmt:formatNumber
                                                    value="${salaryDetail.baseSalary != null ? salaryDetail.baseSalary : 0}"
                                                    pattern="#,##0.00" />
                                            </span>
                                        </div>
                                        <div class="salary-item">
                                            <span class="item-label">课时数</span>
                                            <span class="item-value">${salaryDetail.totalHours != null ?
                                                salaryDetail.totalHours : 0}</span>
                                        </div>
                                        <div class="salary-item">
                                            <span class="item-label">课时费</span>
                                            <span class="item-value">
                                                ￥
                                                <fmt:formatNumber
                                                    value="${salaryDetail.hourlyPay != null ? salaryDetail.hourlyPay : 0}"
                                                    pattern="#,##0.00" />
                                            </span>
                                        </div>
                                        <div class="salary-item total">
                                            <span class="item-label">实发工资</span>
                                            <span class="item-value">
                                                ￥
                                                <fmt:formatNumber
                                                    value="${salaryDetail.totalSalary != null ? salaryDetail.totalSalary : 0}"
                                                    pattern="#,##0.00" />
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="${pageContext.request.contextPath}/static/js/teacher.js"></script>
                <script src="${pageContext.request.contextPath}/static/js/salary.js"></script>
            </body>

            </html>