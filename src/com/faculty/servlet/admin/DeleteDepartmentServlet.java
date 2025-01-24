package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/department/delete")
public class DeleteDepartmentServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            String deptId = request.getParameter("deptId");
            boolean success = departmentDAO.deleteDepartment(deptId);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "删除成功");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "删除失败，该院系可能还有关联的教职工或学生");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}