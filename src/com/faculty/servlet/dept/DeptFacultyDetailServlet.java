package com.faculty.servlet.dept;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dept/faculty/detail")
public class DeptFacultyDetailServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取要查看的教师ID
        int facultyId = Integer.parseInt(request.getParameter("facultyId"));

        // 获取教师信息
        Faculty targetFaculty = facultyDAO.getFacultyById(facultyId);

        // 验证权限（只能查看本院系教师）
        if (targetFaculty == null || !targetFaculty.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
            response.sendRedirect(request.getContextPath() + "/dept/faculty");
            return;
        }

        request.setAttribute("faculty", targetFaculty);
        request.getRequestDispatcher("/dept/faculty_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        try {
            // 从请求体读取更新的教师信息
            Faculty faculty = gson.fromJson(request.getReader(), Faculty.class);

            // 验证权限
            if (!faculty.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"无权修改其他院系的教师信息\"}");
                return;
            }

            // 更新教师信息
            boolean success = facultyDAO.updateFaculty(faculty);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"更新成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"更新失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }
}