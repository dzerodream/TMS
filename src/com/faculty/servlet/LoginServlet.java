package com.faculty.servlet;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String facultyId = request.getParameter("facultyId");
        String password = request.getParameter("password");

        try {
            // 验证登录
            Faculty faculty = facultyDAO.validateLogin(Integer.parseInt(facultyId), password);
            HttpSession session = request.getSession();

            if (faculty != null) {
                session.setAttribute("faculty", faculty);

                // 根据角色ID重定向到不同的页面
                switch (faculty.getRoleId()) {
                    case 1: // 超级管理员
                        response.sendRedirect(request.getContextPath() + "/admin/index");
                        break;
                    case 2: // 院系管理员
                        response.sendRedirect(request.getContextPath() + "/dept/index");
                        break;
                    case 3: // 普通教师
                        response.sendRedirect(request.getContextPath() + "/teacher/index");
                        break;
                    default:
                        request.setAttribute("error", "未知的用户角色");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "工号或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "工号格式不正确");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET请求直接转发到登录页面
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}