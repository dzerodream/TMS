package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/department/batchDelete")
public class BatchDeleteDepartmentServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

            // 将JSON转换为List<String>
            List<String> deptIds = gson.fromJson(sb.toString(), new TypeToken<List<String>>() {
            }.getType());

            boolean success = true;
            for (String deptId : deptIds) {
                if (!departmentDAO.deleteDepartment(deptId)) {
                    success = false;
                    break;
                }
            }

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "批量删除成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "批量删除失败，某些院系可能还有关联的教职工或学生");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}