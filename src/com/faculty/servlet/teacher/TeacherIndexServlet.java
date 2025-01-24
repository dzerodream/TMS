package com.faculty.servlet.teacher;

import com.faculty.dao.ScheduleDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.ScheduleVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/index")
public class TeacherIndexServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取教师的课程统计信息
        int teachingCourses = scheduleDAO.getTeachingCoursesCount(faculty.getFacultyId());
        int totalStudents = scheduleDAO.getTotalStudentsCount(faculty.getFacultyId());
        int weeklyHours = scheduleDAO.getWeeklyHours(faculty.getFacultyId());

        // 获取近期课程安排
        List<ScheduleVO> recentSchedules = scheduleDAO.getRecentSchedules(faculty.getFacultyId());

        request.setAttribute("teachingCourses", teachingCourses);
        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("weeklyHours", weeklyHours);
        request.setAttribute("recentSchedules", recentSchedules);

        request.getRequestDispatcher("/teacher/index.jsp").forward(request, response);
    }
}