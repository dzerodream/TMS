<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="sidebar">
        <div class="logo">教职工管理系统</div>
        <nav class="menu">
            <div class="menu-group">
                <div class="menu-title">教职工管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/admin/faculty">员工管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/department">院系管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/retired">已离职员工管理</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">学生管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/admin/student">学生管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/class">班级管理</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">课程管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/admin/course">课程管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/schedule">课程安排</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">薪酬管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/admin/salary/plan">薪酬计划</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/salary/log">薪酬日志</a></li>
                </ul>
            </div>
        </nav>
    </div>