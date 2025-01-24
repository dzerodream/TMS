package com.faculty.servlet.teacher;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;

@WebServlet("/teacher/profile/*")
public class ProfileServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取最新的教师信息
        Faculty updatedFaculty = facultyDAO.getFacultyById(faculty.getFacultyId());
        request.setAttribute("faculty", updatedFaculty);

        request.getRequestDispatcher("/teacher/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo); // 调试输出

        if ("/update".equals(pathInfo)) {
            handleUpdate(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid endpoint\"}");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            // 读取请求体中的JSON数据
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("Received JSON: " + sb.toString()); // 调试输出

            // 将JSON转换为Faculty对象
            Faculty faculty = gson.fromJson(sb.toString(), Faculty.class);

            // 获取当前登录用户的ID
            Faculty currentFaculty = (Faculty) request.getSession().getAttribute("faculty");
            if (currentFaculty == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "未登录或会话已过期");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            faculty.setFacultyId(currentFaculty.getFacultyId());

            // 基本验证
            if (faculty.getPhoneNumber() == null || faculty.getPhoneNumber().trim().isEmpty() ||
                    faculty.getIdNumber() == null || faculty.getIdNumber().trim().isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "手机号和身份证号不能为空");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // 更新教师信息
            boolean success = facultyDAO.updateFacultyProfile(faculty);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "更新成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "更新失败");
            }
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage()); // 错误日志
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}