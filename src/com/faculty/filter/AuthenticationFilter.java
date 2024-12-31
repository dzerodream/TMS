package com.faculty.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // 不需要过滤的资源
        if (path.equals("/login.jsp") ||
                path.equals("/login") ||
                path.startsWith("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查用户是否已登录
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("faculty") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}