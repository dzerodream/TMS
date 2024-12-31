package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/admin/faculty/update")
public class UpdateFacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
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

            System.out.println("收到的JSON数据: " + sb.toString()); // 添加调试日志

            // 将JSON转换为Faculty对象
            Faculty faculty = gson.fromJson(sb.toString(), Faculty.class);

            // 如果密码为空，设置为null以保持原密码不变
            if (faculty.getPassword() != null && faculty.getPassword().trim().isEmpty()) {
                faculty.setPassword(null);
            }

            System.out.println("转换后的教职工姓名: " + faculty.getFacultyName()); // 添加调试日志

            // 更新教职工信息
            boolean success = facultyDAO.updateFaculty(faculty);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "更新成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "更新失败");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}