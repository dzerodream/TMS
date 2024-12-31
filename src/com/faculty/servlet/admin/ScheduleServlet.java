package com.faculty.servlet.admin;

import com.faculty.dao.ScheduleDAO;
import com.faculty.dao.CourseDAO;
import com.faculty.dao.FacultyDAO;
import com.faculty.dao.ClassDAO;
import com.faculty.dao.StudentDAO;
import com.faculty.vo.ScheduleVO;
import com.faculty.vo.CourseVO;
import com.faculty.vo.ClassVO;
import com.faculty.model.Course;
import com.faculty.model.Faculty;
import com.faculty.model.Student;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

@WebServlet("/admin/schedule/*")
public class ScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取课程安排列表
        List<ScheduleVO> scheduleList = scheduleDAO.getAllSchedules();
        request.setAttribute("scheduleList", scheduleList);

        // 获取课程列表
        CourseDAO courseDAO = new CourseDAO();
        List<CourseVO> courseList = courseDAO.getAllCourses();
        request.setAttribute("courseList", courseList);

        // 获取教师列表
        FacultyDAO facultyDAO = new FacultyDAO();
        List<Faculty> facultyList = facultyDAO.getAllActiveFaculty();
        request.setAttribute("facultyList", facultyList);

        // 获取班级列表
        ClassDAO classDAO = new ClassDAO();
        List<ClassVO> classList = classDAO.getAllClasses();
        request.setAttribute("classList", classList);

        // 获取学生列表
        StudentDAO studentDAO = new StudentDAO();
        List<Student> studentList = studentDAO.getAllStudents();
        request.setAttribute("studentList", studentList);

        request.getRequestDispatcher("/admin/schedule.jsp").forward(request, response);
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
            // 读取请求体中的JSON数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // 解析JSON数据
            JsonObject jsonData = gson.fromJson(sb.toString(), JsonObject.class);
            ScheduleVO schedule = gson.fromJson(jsonData.get("schedule"), ScheduleVO.class);
            JsonArray targetIdsArray = jsonData.getAsJsonArray("targetIds");
            String targetType = jsonData.get("targetType").getAsString();

            // 转换targetIds为List
            List<String> targetIds = new ArrayList<>();
            for (JsonElement element : targetIdsArray) {
                targetIds.add(element.getAsString());
            }

            // 检查时间冲突
            if (scheduleDAO.checkTimeConflict(schedule.getWeekDay(), schedule.getClassTime(),
                    targetIds, targetType, schedule.getStartWeek(), schedule.getEndWeek(), null)) {
                response.getWriter().write("{\"success\":false,\"message\":\"所选时间段已有课程安排\"}");
                return;
            }

            boolean success = scheduleDAO.addSchedule(schedule, targetIds, targetType);

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
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
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
            // 从请求体读取课程安排ID列表
            int[] scheduleIds = gson.fromJson(request.getReader(), int[].class);
            boolean success = scheduleDAO.batchDeleteSchedules(scheduleIds);

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