package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/retired/delete")
public class DeleteRetiredFacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            int facultyId = Integer.parseInt(request.getParameter("facultyId"));
            boolean success = facultyDAO.deleteRetiredFaculty(facultyId);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "删除成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "删除失败");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}