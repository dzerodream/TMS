package com.faculty.servlet.teacher;

import com.faculty.dao.SalaryLogDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.SalaryLogVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/teacher/salary")
public class SalaryServlet extends HttpServlet {
    private SalaryLogDAO salaryLogDAO = new SalaryLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");

        // 获取查询月份，默认为当前月份
        int month;
        String monthParam = request.getParameter("month");
        if (monthParam != null && !monthParam.trim().isEmpty()) {
            try {
                month = Integer.parseInt(monthParam);
                // 验证月份的有效性
                if (month < 1 || month > 12 || (month > 6 && month < 9)) {
                    month = getCurrentValidMonth();
                }
            } catch (NumberFormatException e) {
                month = getCurrentValidMonth();
            }
        } else {
            month = getCurrentValidMonth();
        }

        // 获取教师的薪资明细
        SalaryLogVO salaryDetail = salaryLogDAO.getFacultySalary(faculty.getFacultyId(), month);

        request.setAttribute("currentMonth", month);
        request.setAttribute("salaryDetail", salaryDetail);

        request.getRequestDispatcher("/teacher/salary.jsp").forward(request, response);
    }

    /**
     * 获取当前有效的月份（考虑学期时间）
     */
    private int getCurrentValidMonth() {
        int currentMonth = LocalDate.now().getMonthValue();
        // 如果当前月份是7或8月，返回6月
        if (currentMonth == 2 || currentMonth == 7 || currentMonth == 8) {
            // 2月返回1月，7、8月返回6月
            return currentMonth == 2 ? 1 : 6;
        }
        return currentMonth;
    }
}