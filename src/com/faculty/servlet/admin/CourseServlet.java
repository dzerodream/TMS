package com.faculty.servlet.admin;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Course;
import com.faculty.model.Department;
import com.faculty.vo.CourseVO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/course/*")
public class CourseServlet extends HttpServlet {
    private CourseDAO courseDAO = new CourseDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/detail".equals(pathInfo)) {
            handleDetail(request, response);
        } else {
            // 获取课程列表
            List<CourseVO> courseList = courseDAO.getAllCourses();
            request.setAttribute("courseList", courseList);

            // 获取院系列表
            List<Department> departmentList = departmentDAO.getAllDepartments();
            request.setAttribute("departmentList", departmentList);

            request.getRequestDispatcher("/admin/course.jsp").forward(request, response);
        }
    }

    private void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String courseId = request.getParameter("courseId");
        Course course = courseDAO.getCourseById(courseId);

        response.setContentType("application/json;charset=UTF-8");
        if (course != null) {
            response.getWriter().write(gson.toJson(course));
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"课程不存在\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json;charset=UTF-8");

        if ("/add".equals(pathInfo)) {
            handleAdd(request, response);
        } else if ("/update".equals(pathInfo)) {
            handleUpdate(request, response);
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
            Course course = gson.fromJson(request.getReader(), Course.class);
            boolean success = courseDAO.addCourse(course);

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
            String courseId = request.getParameter("courseId");

            // 检查课程是否已被安排课表
            if (courseDAO.hasSchedule(courseId)) {
                response.getWriter().write("{\"success\":false,\"message\":\"该课程已被安排课表，不能删除\"}");
                return;
            }

            boolean success = courseDAO.deleteCourse(courseId);

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
            // 从请求体读取课程ID列表
            String[] courseIds = gson.fromJson(request.getReader(), String[].class);

            // 检查是否有课程已被安排课表
            for (String courseId : courseIds) {
                if (courseDAO.hasSchedule(courseId)) {
                    response.getWriter().write("{\"success\":false,\"message\":\"选中的课程中有已被安排课表的课程，不能删除\"}");
                    return;
                }
            }

            boolean success = courseDAO.batchDeleteCourses(courseIds);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"批量删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"批量删除失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Course course = gson.fromJson(request.getReader(), Course.class);
            boolean success = courseDAO.updateCourse(course);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"修改成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"修改失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }
}