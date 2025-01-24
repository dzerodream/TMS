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
import java.util.List;

@WebServlet("/dept/faculty")
public class DeptFacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员信息
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取当前院系的所有教师
        List<Faculty> facultyList = facultyDAO.getFacultyByDepartment(currentFaculty.getDepartmentId());
        request.setAttribute("facultyList", facultyList);

        request.getRequestDispatcher("/dept/faculty.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json;charset=UTF-8");

        if ("leave".equals(action)) {
            handleLeave(request, response);
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
        }
    }

    private void handleLeave(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int facultyId = Integer.parseInt(request.getParameter("facultyId"));
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        // 验证要离职的教师是否属于当前院系
        Faculty targetFaculty = facultyDAO.getFacultyById(facultyId);
        if (targetFaculty == null || !targetFaculty.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
            response.getWriter().write("{\"success\":false,\"message\":\"无权操作其他院系的教师\"}");
            return;
        }

        boolean success = facultyDAO.updateFacultyStatus(facultyId, 0); // 0表示离职状态
        if (success) {
            response.getWriter().write("{\"success\":true,\"message\":\"操作成功\"}");
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"操作失败\"}");
        }
    }
}