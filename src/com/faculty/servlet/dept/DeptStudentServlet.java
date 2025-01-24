package com.faculty.servlet.dept;

import com.faculty.dao.StudentDAO;
import com.faculty.dao.DepartmentDAO;
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

@WebServlet("/dept/student")
public class DeptStudentServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取本院系的学生列表
        List<Student> studentList = studentDAO.getStudentsByDepartment(currentFaculty.getDepartmentId());
        request.setAttribute("studentList", studentList);

        // 获取班级列表
        List<ClassInfo> classList = studentDAO.getClassesByDepartment(currentFaculty.getDepartmentId());
        request.setAttribute("classList", classList);

        // 设置当前年份
        request.setAttribute("currentYear", java.time.Year.now().getValue());

        request.getRequestDispatcher("/dept/student.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json;charset=UTF-8");
        Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");

        try {
            switch (action) {
                case "graduate":
                    handleGraduate(request, response, currentFaculty);
                    break;
                case "delete":
                    handleDelete(request, response, currentFaculty);
                    break;
                case "add":
                    handleAdd(request, response, currentFaculty);
                    break;
                default:
                    response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleGraduate(HttpServletRequest request, HttpServletResponse response, Faculty currentFaculty)
            throws IOException {
        String studentId = request.getParameter("studentId");
        Student student = studentDAO.getStudentById(studentId);

        // 验证权限
        if (student == null || !student.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
            response.getWriter().write("{\"success\":false,\"message\":\"无权操作其他院系的学生\"}");
            return;
        }

        boolean success = studentDAO.updateStudentStatus(studentId, 0); // 0表示已毕业
        if (success) {
            response.getWriter().write("{\"success\":true,\"message\":\"操作成功\"}");
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"操作失败\"}");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, Faculty currentFaculty)
            throws IOException {
        String studentId = request.getParameter("studentId");
        Student student = studentDAO.getStudentById(studentId);

        // 验证权限
        if (student == null || !student.getDepartmentId().equals(currentFaculty.getDepartmentId())) {
            response.getWriter().write("{\"success\":false,\"message\":\"无权操作其他院系的学生\"}");
            return;
        }

        boolean success = studentDAO.deleteStudent(studentId);
        if (success) {
            response.getWriter().write("{\"success\":true,\"message\":\"删除成功\"}");
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"删除失败\"}");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response, Faculty currentFaculty)
            throws IOException {
        try {
            Student student = gson.fromJson(request.getReader(), Student.class);

            // 设置院系ID为当前管理员的院系
            student.setDepartmentId(currentFaculty.getDepartmentId());
            // 设置初始状态为在校
            student.setStatus(1);

            // 生成学号：年份(2位) + 院系编号(2位) + 序号(4位)
            String year = String.valueOf(student.getEnrollmentYear()).substring(2);
            String deptCode = currentFaculty.getDepartmentId().substring(0, 2);
            String sequence = String.format("%04d", studentDAO.getNextSequence(currentFaculty.getDepartmentId()));
            student.setStudentId(year + deptCode + sequence);

            // 验证班级是否属于当前院系
            if (!validateClassBelongsToDepartment(student.getClassId(), currentFaculty.getDepartmentId())) {
                response.getWriter().write("{\"success\":false,\"message\":\"无效的班级选择\"}");
                return;
            }

            boolean success = studentDAO.addStudent(student);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"添加成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"添加失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    // 验证班级是否属于指定院系
    private boolean validateClassBelongsToDepartment(String classId, String departmentId) {
        List<ClassInfo> classList = studentDAO.getClassesByDepartment(departmentId);
        return classList.stream()
                .anyMatch(classInfo -> classInfo.getClassId().equals(classId));
    }
}