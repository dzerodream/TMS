package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Department;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/department/detail")
public class DepartmentDetailServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            String deptId = request.getParameter("deptId");
            Department department = departmentDAO.getDepartmentById(deptId);

            if (department != null) {
                jsonResponse.addProperty("success", true);
                JsonObject deptJson = new JsonObject();
                deptJson.addProperty("deptId", department.getDeptId());
                deptJson.addProperty("deptName", department.getDeptName());
                deptJson.addProperty("numOfPeople", department.getNumOfPeople());
                jsonResponse.add("data", deptJson);
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "未找到该院系信息");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "系统错误：" + e.getMessage());
            e.printStackTrace();
        }

        response.getWriter().write(jsonResponse.toString());
    }
}