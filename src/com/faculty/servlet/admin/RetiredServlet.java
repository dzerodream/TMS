package com.faculty.servlet.admin;

import com.faculty.dao.FacultyDAO;
import com.faculty.model.Faculty;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/retired")
public class RetiredServlet extends HttpServlet {
    private FacultyDAO facultyDAO = new FacultyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取离职教职工列表
        List<Faculty> retiredList = facultyDAO.getRetiredFaculty();
        request.setAttribute("retiredList", retiredList);

        request.getRequestDispatcher("/admin/retired.jsp").forward(request, response);
    }
}