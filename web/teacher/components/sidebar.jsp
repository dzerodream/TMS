<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="sidebar">
        <div class="logo">教职工管理系统</div>
        <nav class="menu">
            <div class="menu-group">
                <div class="menu-title">个人信息</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/teacher/profile">基本信息</a></li>
                    <li><a href="${pageContext.request.contextPath}/teacher/salary">薪资查看</a></li>
                </ul>
            </div>
            <div class="menu-group">
                <div class="menu-title">课程管理</div>
                <ul class="menu-items">
                    <li><a href="${pageContext.request.contextPath}/teacher/schedule">我的课表</a></li>
                    <li><a href="${pageContext.request.contextPath}/teacher/students">学生名单</a></li>
                </ul>
            </div>
        </nav>
    </div>