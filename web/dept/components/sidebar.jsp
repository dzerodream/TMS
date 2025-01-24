<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="sidebar">
        <div class="logo">教职工管理系统</div>
        <nav class="menu">
            <div class="menu-group">
                <div class="menu-title">教师管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/dept/faculty">教师管理</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">学生管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/dept/student">学生管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/dept/class">班级管理</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">课程管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/dept/course">课程信息</a></li>
                    <li><a href="${pageContext.request.contextPath}/dept/schedule">课程安排</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">薪资管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/dept/salary/log">薪资查看</a></li>
                </ul>
            </div>
        </nav>
    </div>