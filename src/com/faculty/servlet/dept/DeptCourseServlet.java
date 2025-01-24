package com.faculty.servlet.dept;

import com.faculty.dao.CourseDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.CourseVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/dept/course")
public class DeptCourseServlet extends HttpServlet {
    private CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员信息
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
        String departmentId = faculty.getDepartmentId();

        // 获取该院系的课程列表
        List<CourseVO> allCourses = courseDAO.getAllCourses();
        List<CourseVO> deptCourses = allCourses.stream()
                .filter(c -> c.getDeptId().equals(departmentId))
                .collect(Collectors.toList());

        request.setAttribute("courseList", deptCourses);
        request.getRequestDispatcher("/dept/course.jsp").forward(request, response);
    }
}