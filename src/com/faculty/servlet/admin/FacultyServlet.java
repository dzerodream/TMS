package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.faculty.dao.DepartmentDAO;
import com.faculty.model.Faculty;
import com.faculty.model.Department;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/faculty")
public class FacultyServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取教职工列表
        List<Faculty> facultyList = facultyDAO.getAllFaculty();
        request.setAttribute("facultyList", facultyList);

        // 获取院系列表
        List<Department> departmentList = departmentDAO.getAllDepartments();
        System.out.println("部门列表大小: " + departmentList.size());
        for (Department dept : departmentList) {
            System.out.println("部门ID: " + dept.getDeptId() + ", 名称: " + dept.getDeptName());
        }
        request.setAttribute("departmentList", departmentList);

        request.getRequestDispatcher("/admin/faculty.jsp").forward(request, response);
    }
}