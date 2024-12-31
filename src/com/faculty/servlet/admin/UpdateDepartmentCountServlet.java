package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/department/updateCount")
public class UpdateDepartmentCountServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            boolean success = departmentDAO.updateAllDepartmentPeopleCount();

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "院系人数更新成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "院系人数更新失败");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}