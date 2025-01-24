package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;

@WebServlet("/admin/faculty/batchDelete")
public class BatchDeleteFacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        // 读取请求体中的JSON数据
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JsonObject jsonResponse = new JsonObject();

        try {
            // 将JSON转换为List<Integer>
            List<Integer> facultyIds = gson.fromJson(sb.toString(),
                    new TypeToken<List<Integer>>() {
                    }.getType());

            if (facultyIds.isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "请选择要删除的成员");
                return;
            }

            boolean success = facultyDAO.batchDeleteFaculty(facultyIds);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "批量删除成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "批量删除失败");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}