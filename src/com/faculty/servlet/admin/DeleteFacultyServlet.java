package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/faculty/delete")
public class DeleteFacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            int facultyId = Integer.parseInt(request.getParameter("facultyId"));
            boolean success = facultyDAO.deleteFaculty(facultyId);

            jsonResponse.addProperty("success", success);
            jsonResponse.addProperty("message", success ? "删除成功" : "删除失败");

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}