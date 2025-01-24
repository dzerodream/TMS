package com.faculty.servlet.dept;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.FacultyDAO;
import com.faculty.dao.ScheduleDAO;
import com.faculty.dao.ClassDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.ScheduleVO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@WebServlet("/dept/schedule/*")
public class DeptScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private FacultyDAO facultyDAO = new FacultyDAO();
    private ClassDAO classDAO = new ClassDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员信息
        Faculty admin = (Faculty) request.getSession().getAttribute("faculty");
        String departmentId = admin.getDepartmentId();

        // 获取本院系的课程安排
        List<ScheduleVO> allSchedules = scheduleDAO.getAllSchedules();
        List<ScheduleVO> deptSchedules = allSchedules.stream()
                .filter(s -> s.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());

        // 获取本院系的课程
        request.setAttribute("courseList", courseDAO.getAllCourses().stream()
                .filter(c -> c.getDeptId().equals(departmentId))
                .collect(Collectors.toList()));

        // 获取本院系的教师
        request.setAttribute("facultyList", facultyDAO.getAllActiveFaculty().stream()
                .filter(f -> f.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList()));

        // 获取本院系的班级
        request.setAttribute("classList", classDAO.getAllClasses().stream()
                .filter(c -> c.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList()));

        request.setAttribute("scheduleList", deptSchedules);
        request.getRequestDispatcher("/dept/schedule.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json;charset=UTF-8");

        if ("/add".equals(pathInfo)) {
            handleAdd(request, response);
        } else if ("/delete".equals(pathInfo)) {
            handleDelete(request, response);
        } else if ("/batchDelete".equals(pathInfo)) {
            handleBatchDelete(request, response);
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Faculty admin = (Faculty) request.getSession().getAttribute("faculty");
            JsonObject jsonData = gson.fromJson(request.getReader(), JsonObject.class);

            // 验证课程和教师是否属于本院系
            String courseId = jsonData.get("courseId").getAsString();
            int facultyId = jsonData.get("facultyId").getAsInt();

            if (!courseDAO.getCourseById(courseId).getDeptId().equals(admin.getDepartmentId()) ||
                    !facultyDAO.getFacultyById(facultyId).getDepartmentId().equals(admin.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"只能安排本院系的课程和教师\"}");
                return;
            }

            // 创建ScheduleVO对象
            ScheduleVO schedule = new ScheduleVO();
            schedule.setCourseId(courseId);
            schedule.setFacultyId(facultyId);
            schedule.setStartWeek(jsonData.get("startWeek").getAsInt());
            schedule.setEndWeek(jsonData.get("endWeek").getAsInt());
            schedule.setWeekDay(jsonData.get("weekDay").getAsInt());
            schedule.setClassTime(jsonData.get("classTime").getAsString());
            schedule.setLocation(jsonData.get("location").getAsString());

            // 获取上课对象信息
            String targetType = jsonData.get("targetType").getAsString();
            List<String> targets = new ArrayList<>();
            jsonData.get("targets").getAsJsonArray().forEach(target -> targets.add(target.getAsString()));

            boolean success = scheduleDAO.addSchedule(schedule, targets, targetType);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"添加成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"添加失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Faculty admin = (Faculty) request.getSession().getAttribute("faculty");
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));

            // 验证是否是本院系的课程安排
            ScheduleVO schedule = scheduleDAO.getAllSchedules().stream()
                    .filter(s -> s.getScheduleId() == scheduleId)
                    .findFirst()
                    .orElse(null);

            if (schedule == null || !schedule.getDepartmentId().equals(admin.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"只能删除本院系的课程安排\"}");
                return;
            }

            boolean success = scheduleDAO.deleteSchedule(scheduleId);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"删除失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleBatchDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Faculty admin = (Faculty) request.getSession().getAttribute("faculty");
            int[] scheduleIds = gson.fromJson(request.getReader(), int[].class);

            // 验证是否都是本院系的课程安排
            for (int scheduleId : scheduleIds) {
                ScheduleVO schedule = scheduleDAO.getAllSchedules().stream()
                        .filter(s -> s.getScheduleId() == scheduleId)
                        .findFirst()
                        .orElse(null);

                if (schedule == null || !schedule.getDepartmentId().equals(admin.getDepartmentId())) {
                    response.getWriter().write("{\"success\":false,\"message\":\"只能删除本院系的课程安排\"}");
                    return;
                }
            }

            boolean success = scheduleDAO.batchDeleteSchedule(scheduleIds);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"批量删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"批量删除失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }
}