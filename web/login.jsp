<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>教职工管理系统 - 登录</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/login.css">
    </head>

    <body>
        <div class="login-container">
            <div class="login-box">
                <h2>教职工管理系统</h2>
                <form action="login" method="post" id="loginForm">
                    <div class="input-group">
                        <input type="text" name="facultyId" required placeholder="教职工编号">
                    </div>
                    <div class="input-group">
                        <input type="password" name="password" required placeholder="密码">
                    </div>
                    <div class="error-message" id="errorMsg">
                        <%= request.getAttribute("error") !=null ? request.getAttribute("error") : "" %>
                    </div>
                    <button type="submit">登录</button>
                </form>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/static/js/login.js"></script>
    </body>

    </html>