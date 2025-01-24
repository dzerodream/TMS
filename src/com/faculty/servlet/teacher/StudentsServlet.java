package com.faculty.servlet.teacher;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.StudentDAO;
import com.faculty.model.Course;
import com.faculty.model.Faculty;
import com.faculty.vo.StudentVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/students")
public class StudentsServlet extends HttpServlet {
    private CourseDAO courseDAO = new CourseDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
        if (faculty == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 获取教师的所有课程
        List<Course> courses = courseDAO.getTeacherCourses(faculty.getFacultyId());
        request.setAttribute("courses", courses);

        // 获取当前选中的课程
        String courseId = request.getParameter("courseId");
        if (courseId == null && !courses.isEmpty()) {
            courseId = courses.get(0).getCourseId();
        }
        request.setAttribute("currentCourse", courseId);

        // 获取学生名单
        if (courseId != null) {
            List<StudentVO> students = studentDAO.getCourseStudents(courseId);
            request.setAttribute("students", students);
        }

        request.getRequestDispatcher("/teacher/students.jsp").forward(request, response);
    }
}