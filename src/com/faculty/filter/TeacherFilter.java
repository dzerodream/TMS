package com.faculty.filter;

import com.faculty.model.Faculty;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/teacher/*")
public class TeacherFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 检查是否登录且是否为普通教职工
        if (session != null && session.getAttribute("faculty") != null) {
            Faculty faculty = (Faculty) session.getAttribute("faculty");
            if (faculty.getRoleId() == 3) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 如果不是普通教职工，重定向到登录页面
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}