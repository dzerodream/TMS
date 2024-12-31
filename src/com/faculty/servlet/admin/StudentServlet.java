package com.faculty.servlet.admin;

import com.faculty.dao.StudentDAO;
import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Student;
import com.faculty.model.Department;
import com.faculty.model.ClassInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@WebServlet("/admin/student/*")
public class StudentServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/detail".equals(pathInfo)) {
            handleDetail(request, response);
        } else if ("/classes".equals(pathInfo)) {
            handleGetClasses(request, response);
        } else {
            // 获取学生列表
            List<Student> studentList = studentDAO.getAllStudents();
            request.setAttribute("studentList", studentList);

            // 获取院系列表
            List<Department> departmentList = departmentDAO.getAllDepartments();
            request.setAttribute("departmentList", departmentList);

            // 获取班级列表
            List<ClassInfo> classList = studentDAO.getAllClasses();
            request.setAttribute("classList", classList);

            request.getRequestDispatcher("/admin/student.jsp").forward(request, response);
        }
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
        } else if ("/update".equals(pathInfo)) {
            handleUpdate(request, response);
        } else if ("/toggleStatus".equals(pathInfo)) {
            handleToggleStatus(request, response);
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // 从请求体读取JSON数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // 打印接收到的数据
            System.out.println("Received JSON: " + sb.toString());

            // 将JSON转换为Student对象
            Student student = gson.fromJson(sb.toString(), Student.class);

            // 验证必要字段
            if (student.getStudentId() == null || student.getStudentId().trim().isEmpty() ||
                    student.getStudentName() == null || student.getStudentName().trim().isEmpty() ||
                    student.getGender() == null || student.getGender().trim().isEmpty() ||
                    student.getDepartmentId() == null || student.getDepartmentId().trim().isEmpty() ||
                    student.getClassId() == null || student.getClassId().trim().isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"请填写所有必要信息\"}");
                return;
            }

            // 打印转换后的对象
            System.out.println("Student object: " + student.getStudentId() + ", " + student.getStudentName());

            // 添加学生
            boolean success = studentDAO.addStudent(student);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"添加成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"添加失败，可能是学号重复\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String studentId = request.getParameter("studentId");
            System.out.println("Handling delete request for student ID: " + studentId);

            if (studentId == null || studentId.trim().isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"学号不能为空\"}");
                return;
            }

            boolean success = studentDAO.deleteStudent(studentId);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"删除失败，该学生可能不存在\"}");
            }
        } catch (Exception e) {
            System.out.println("Delete error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleBatchDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // 从请求体读取JSON数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            System.out.println("Received batch delete request: " + sb.toString());

            // 将JSON转换为List<String>
            List<String> studentIds = gson.fromJson(sb.toString(), List.class);

            if (studentIds == null || studentIds.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"请选择要删除的学生\"}");
                return;
            }

            boolean success = studentDAO.batchDeleteStudents(studentIds);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"批量删除成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"批量删除失败，请检查学生信息\"}");
            }
        } catch (Exception e) {
            System.out.println("Batch delete error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String studentId = request.getParameter("studentId");
        System.out.println("Getting details for student ID: " + studentId); // 添加日志

        Student student = studentDAO.getStudentById(studentId);
        System.out.println("Retrieved student: " + (student != null ? student.getStudentName() : "null")); // 添加日志

        response.setContentType("application/json;charset=UTF-8");
        if (student != null) {
            String json = gson.toJson(student);
            System.out.println("Sending JSON: " + json); // 添加日志
            response.getWriter().write(json);
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"未找到该学生\"}");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Student student = gson.fromJson(request.getReader(), Student.class);
            boolean success = studentDAO.updateStudent(student);

            response.setContentType("application/json;charset=UTF-8");
            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"修改成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"修改失败\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleToggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // 从请求体读取JSON数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            System.out.println("Received toggle status request: " + sb.toString());

            // 解析JSON数据
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            String studentId = jsonObject.get("studentId").getAsString();
            int status = jsonObject.get("status").getAsInt();

            // 更新状态
            boolean success = studentDAO.updateStudentStatus(studentId, status);

            response.setContentType("application/json;charset=UTF-8");
            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"状态更新成功\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"状态更新失败\"}");
            }
        } catch (Exception e) {
            System.out.println("Toggle status error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }

    private void handleGetClasses(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String departmentId = request.getParameter("departmentId");
        List<ClassInfo> classList = studentDAO.getClassesByDepartment(departmentId);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(classList));
    }
}