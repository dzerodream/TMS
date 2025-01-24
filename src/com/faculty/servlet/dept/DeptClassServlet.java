package com.faculty.servlet.dept;

import com.faculty.dao.ClassDAO;
import com.faculty.model.ClassInfo;
import com.faculty.model.Faculty;
import com.faculty.vo.ClassVO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/dept/class/*")
public class DeptClassServlet extends HttpServlet {
    private ClassDAO classDAO = new ClassDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员信息
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
        String departmentId = faculty.getDepartmentId();

        // 获取该院系的班级列表
        List<ClassVO> allClasses = classDAO.getAllClasses();
        List<ClassVO> deptClasses = allClasses.stream()
                .filter(c -> c.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());

        request.setAttribute("classList", deptClasses);
        request.getRequestDispatcher("/dept/class.jsp").forward(request, response);
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
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
            ClassInfo classInfo = gson.fromJson(request.getReader(), ClassInfo.class);

            // 确保只能添加本院系的班级
            if (!classInfo.getDepartmentId().equals(faculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"只能添加本院系的班级\"}");
                return;
            }

            boolean success = classDAO.addClass(classInfo);

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
            Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
            String classId = request.getParameter("classId");

            // 检查是否是本院系的班级
            ClassVO classInfo = classDAO.getAllClasses().stream()
                    .filter(c -> c.getClassId().equals(classId))
                    .findFirst()
                    .orElse(null);

            if (classInfo == null || !classInfo.getDepartmentId().equals(faculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"只能删除本院系的班级\"}");
                return;
            }

            // 检查班级是否有学生
            if (classDAO.hasStudents(classId)) {
                response.getWriter().write("{\"success\":false,\"message\":\"该班级还有学生，不能删除\"}");
                return;
            }

            boolean success = classDAO.deleteClass(classId);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"删除失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }
}