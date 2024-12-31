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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String facultyId = request.getParameter("facultyId");
        String password = request.getParameter("password");

        try {
            FacultyDAO facultyDAO = new FacultyDAO();
            Faculty faculty = facultyDAO.validateLogin(Integer.parseInt(facultyId), password);

            if (faculty != null) {
                HttpSession session = request.getSession();
                session.setAttribute("faculty", faculty);

                // 根据角色重定向到不同页面
                if (faculty.getRoleId() == 1) { // 超级管理员
                    response.sendRedirect(request.getContextPath() + "/admin/index.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                request.setAttribute("error", "教职工编号或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "教职工编号格式不正确");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}