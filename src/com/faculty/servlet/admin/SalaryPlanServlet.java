package com.faculty.servlet.admin;

import com.faculty.dao.SalaryPlanDAO;
import com.faculty.vo.SalaryPlanVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet({ "/admin/salary/plan", "/admin/salary/plan/update" })
public class SalaryPlanServlet extends HttpServlet {
    private SalaryPlanDAO salaryPlanDAO = new SalaryPlanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<SalaryPlanVO> salaryPlans = salaryPlanDAO.getAllSalaryPlans();
        request.setAttribute("salaryPlans", salaryPlans);
        request.getRequestDispatcher("/admin/salary_plan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        System.out.println("Servlet Path: " + servletPath);
        System.out.println("Request URI: " + request.getRequestURI());
        response.setContentType("application/json;charset=UTF-8");

        if ("/admin/salary/plan/update".equals(servletPath)) {
            System.out.println("Handling update request");
            handleUpdate(request, response);
        } else {
            System.out.println("Unknown action: " + servletPath);
            response.getWriter().write("{\"success\":false,\"message\":\"Unknown action\"}");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            BigDecimal baseSalary = new BigDecimal(request.getParameter("baseSalary"));

            if (baseSalary.compareTo(BigDecimal.ZERO) <= 0) {
                response.getWriter().write("{\"success\":false,\"message\":\"基本工资必须大于0\"}");
                return;
            }

            boolean success = salaryPlanDAO.updateBaseSalary(roleId, baseSalary);

            String jsonResponse = String.format(
                    "{\"success\":%b,\"message\":\"%s\"}",
                    success,
                    success ? "更新成功" : "更新失败");
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"系统错误：" + e.getMessage() + "\"}");
        }
    }
}