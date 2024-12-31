package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/faculty/detail")
public class FacultyDetailServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            int facultyId = Integer.parseInt(request.getParameter("facultyId"));
            Faculty faculty = facultyDAO.getFacultyById(facultyId);

            if (faculty != null) {
                jsonResponse.addProperty("success", true);
                // 将Faculty对象转换为JSON
                JsonObject facultyJson = new JsonObject();
                facultyJson.addProperty("facultyId", faculty.getFacultyId());
                facultyJson.addProperty("facultyName", faculty.getFacultyName());
                facultyJson.addProperty("facultyNumber", String.format("%04d", faculty.getFacultyId()));
                facultyJson.addProperty("departmentId", faculty.getDepartmentId());
                facultyJson.addProperty("phoneNumber", faculty.getPhoneNumber());
                facultyJson.addProperty("idNumber", faculty.getIdNumber());
                facultyJson.addProperty("roleId", faculty.getRoleId());
                facultyJson.addProperty("password", faculty.getPassword());

                jsonResponse.add("data", facultyJson);
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "未找到该教职工信息");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}