package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Department;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/admin/department/add")
public class AddDepartmentServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
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

            // 将JSON转换为Department对象
            Department department = gson.fromJson(sb.toString(), Department.class);

            // 添加院系
            boolean success = departmentDAO.addDepartment(department);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "添加院系成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "添加院系失败");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}