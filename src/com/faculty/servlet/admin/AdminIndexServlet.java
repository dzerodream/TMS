package com.faculty.servlet.admin;

import com.faculty.model.Faculty;
import com.faculty.dao.*;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/index")
public class AdminIndexServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");

        // 检查是否为超级管理员
        if (faculty == null || faculty.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 获取统计数据
        Map<String, Integer> stats = new HashMap<>();
        stats.put("facultyCount", facultyDAO.getActiveFacultyCount());
        stats.put("departmentCount", departmentDAO.getDepartmentCount());
        stats.put("studentCount", studentDAO.getActiveStudentCount());
        stats.put("courseCount", courseDAO.getCourseCount());

        request.setAttribute("stats", stats);
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }
}