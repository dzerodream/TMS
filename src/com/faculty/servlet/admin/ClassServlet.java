package com.faculty.servlet.admin;

import com.faculty.dao.ClassDAO;
import com.faculty.dao.DepartmentDAO;
import com.faculty.model.ClassInfo;
import com.faculty.model.Department;
import com.faculty.vo.ClassVO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/class/*")
public class ClassServlet extends HttpServlet {
    private ClassDAO classDAO = new ClassDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取班级列表
        List<ClassVO> classList = classDAO.getAllClasses();
        request.setAttribute("classList", classList);

        // 获取院系列表
        List<Department> departmentList = departmentDAO.getAllDepartments();
        request.setAttribute("departmentList", departmentList);

        request.getRequestDispatcher("/admin/class.jsp").forward(request, response);
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
            ClassInfo classInfo = gson.fromJson(request.getReader(), ClassInfo.class);
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
            String classId = request.getParameter("classId");

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