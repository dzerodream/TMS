package com.faculty.servlet.dept;

import com.faculty.dao.StudentDAO;
import com.faculty.model.Student;
import com.faculty.model.Faculty;
import com.faculty.model.ClassInfo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dept/student/detail")
public class DeptStudentDetailServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取要查看的学生ID
        String studentId = request.getParameter("studentId");

        // 获取学生信息
        Student student = studentDAO.getStudentById(studentId);

        // 验证权限（只能查看本院系学生）
        if (student == null || !student.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
            response.sendRedirect(request.getContextPath() + "/dept/student");
            return;
        }

        // 获取班级列表（用于班级选择）
        List<ClassInfo> classList = studentDAO.getClassesByDepartment(currentFaculty.getDepartmentId());

        request.setAttribute("student", student);
        request.setAttribute("classList", classList);
        request.getRequestDispatcher("/dept/student_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        try {
            // 从请求体读取更新的学生信息
            Student student = gson.fromJson(request.getReader(), Student.class);

            // 验证权限
            if (!student.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"无权修改其他院系的学生信息\"}");
                return;
            }

            // 验证班级是否属于当前院系
            if (!validateClassBelongsToDepartment(student.getClassId(), currentFaculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"无效的班级选择\"}");
                return;
            }

            // 更新学生信息
            boolean success = studentDAO.updateStudent(student);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"更新成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"更新失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private boolean validateClassBelongsToDepartment(String classId, String departmentId) {
        List<ClassInfo> classList = studentDAO.getClassesByDepartment(departmentId);
        return classList.stream()
                .anyMatch(classInfo -> classInfo.getClassId().equals(classId));
    }
}