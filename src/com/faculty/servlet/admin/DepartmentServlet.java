package com.faculty.servlet.admin;

import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Department;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/department")
public class DepartmentServlet extends HttpServlet {
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 自动更新院系人数
        departmentDAO.updateAllDepartmentPeopleCount();

        // 获取院系列表
        List<Department> departmentList = departmentDAO.getAllDepartments();
        request.setAttribute("departmentList", departmentList);

        request.getRequestDispatcher("/admin/department.jsp").forward(request, response);
    }
}