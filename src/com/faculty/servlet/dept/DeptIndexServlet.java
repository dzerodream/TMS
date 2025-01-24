package com.faculty.servlet.dept;

import com.faculty.dao.FacultyDAO;
import com.faculty.dao.CourseDAO;
import com.faculty.dao.StudentDAO;
import com.faculty.model.Faculty;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dept/index")
public class DeptIndexServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty admin = (Faculty) request.getSession().getAttribute("faculty");
        String departmentId = admin.getDepartmentId();

        // 获取本院系的统计数据
        long facultyCount = facultyDAO.getAllActiveFaculty().stream()
                .filter(f -> f.getDepartmentId().equals(departmentId))
                .count();

        long studentCount = studentDAO.getAllStudents().stream()
                .filter(s -> s.getDepartmentId().equals(departmentId))
                .count();

        long courseCount = courseDAO.getAllCourses().stream()
                .filter(c -> c.getDeptId().equals(departmentId))
                .count();

        // 设置统计数据
        request.setAttribute("facultyCount", facultyCount);
        request.setAttribute("studentCount", studentCount);
        request.setAttribute("courseCount", courseCount);
        request.setAttribute("departmentCount", 1); // 院系管理员只管理一个院系

        request.getRequestDispatcher("/dept/index.jsp").forward(request, response);
    }
}