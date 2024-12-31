package com.faculty.servlet.admin;

import com.faculty.dao.SalaryLogDAO;
import com.faculty.vo.SalaryLogVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/salary/log")
public class SalaryLogServlet extends HttpServlet {
    private SalaryLogDAO salaryLogDAO = new SalaryLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取月份参数，如果没有则使用当前月份
        int month;
        String monthParam = request.getParameter("month");
        if (monthParam != null && !monthParam.isEmpty()) {
            month = Integer.parseInt(monthParam);
        } else {
            month = LocalDate.now().getMonthValue();
        }

        List<SalaryLogVO> salaryLogs = salaryLogDAO.getMonthlySalary(month);
        request.setAttribute("salaryLogs", salaryLogs);
        request.setAttribute("currentMonth", month);
        request.getRequestDispatcher("/admin/salary_log.jsp").forward(request, response);
    }
}