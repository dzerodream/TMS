package com.faculty.servlet.dept;

import com.faculty.dao.SalaryLogDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.SalaryLogVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Calendar;
import java.util.stream.Collectors;

@WebServlet("/dept/salary/log")
public class DeptSalaryLogServlet extends HttpServlet {
    private SalaryLogDAO salaryLogDAO = new SalaryLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的院系管理员信息
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
        String departmentId = faculty.getDepartmentId();

        // 获取查询月份，默认为当前月份
        int month;
        try {
            month = Integer.parseInt(request.getParameter("month"));
        } catch (NumberFormatException e) {
            month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        }

        // 获取该院系的薪资记录
        List<SalaryLogVO> allSalaryLogs = salaryLogDAO.getMonthlySalary(month);
        List<SalaryLogVO> deptSalaryLogs = allSalaryLogs.stream()
                .filter(log -> log.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());

        request.setAttribute("currentMonth", month);
        request.setAttribute("salaryLogs", deptSalaryLogs);
        request.getRequestDispatcher("/dept/salary_log.jsp").forward(request, response);
    }
}