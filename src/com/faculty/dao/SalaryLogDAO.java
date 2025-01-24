package com.faculty.dao;

import com.faculty.model.Faculty;
import com.faculty.vo.SalaryLogVO;
import com.faculty.util.DBUtil;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SalaryLogDAO {

    private int[] calculateWeekRange(int month) {
        // 确定学期起始月份
        boolean isFirstSemester = (month >= 9 && month <= 12) || month == 1;

        // 计算从学期开始到现在的周数
        int monthDiff;
        if (isFirstSemester) {
            monthDiff = month >= 9 ? month - 9 : month + 4;
        } else {
            monthDiff = month - 3;
        }

        // 计算开始周和结束周
        int startWeek = monthDiff * 4 + 1;
        int endWeek = startWeek + 3; // 每月4周

        // 调试输出
        System.out.println("Month: " + month + ", StartWeek: " + startWeek + ", EndWeek: " + endWeek);

        return new int[] { startWeek, endWeek };
    }

    public List<SalaryLogVO> getMonthlySalary(int month) {
        List<SalaryLogVO> salaryLogs = new ArrayList<>();
        FacultyDAO facultyDAO = new FacultyDAO();
        List<Faculty> facultyList = facultyDAO.getAllActiveFaculty();

        // 获取指定月份的周数范围
        int[] weekRange = calculateWeekRange(month);
        int startWeek = weekRange[0];
        int endWeek = weekRange[1];

        // SQL查询，统计在指定周范围内每周有课的次数
        String sql = "WITH RECURSIVE weeks AS (" +
                "  SELECT ? as week " +
                "  UNION ALL " +
                "  SELECT week + 1 FROM weeks WHERE week < ? " +
                "), " +
                "weekly_classes AS ( " +
                "  SELECT f.faculty_id, f.faculty_name, f.department_id, r.base_salary, w.week, " +
                "  COUNT(DISTINCT CONCAT(s.week_day, '_', s.class_time)) as classes " +
                "  FROM t_faculty f " +
                "  JOIN t_role r ON f.role_id = r.role_id " +
                "  CROSS JOIN weeks w " +
                "  LEFT JOIN t_schedule s ON f.faculty_id = s.faculty_id " +
                "  AND s.start_week <= w.week AND s.end_week >= w.week " +
                "  WHERE f.faculty_id = ? " +
                "  GROUP BY f.faculty_id, f.faculty_name, f.department_id, r.base_salary, w.week " +
                ") " +
                "SELECT faculty_id, faculty_name, department_id, base_salary, " +
                "SUM(classes) as total_classes " +
                "FROM weekly_classes " +
                "GROUP BY faculty_id, faculty_name, department_id, base_salary";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Faculty faculty : facultyList) {
                pstmt.setInt(1, startWeek);
                pstmt.setInt(2, endWeek);
                pstmt.setInt(3, faculty.getFacultyId());

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int totalClasses = rs.getInt("total_classes");
                    BigDecimal baseSalary = rs.getBigDecimal("base_salary");
                    BigDecimal hourlyRate = new BigDecimal("100.00"); // 每课时100元
                    BigDecimal hourlyPay = hourlyRate.multiply(new BigDecimal(totalClasses));

                    SalaryLogVO salaryLog = new SalaryLogVO();
                    salaryLog.setFacultyId(faculty.getFacultyId());
                    salaryLog.setFacultyName(faculty.getFacultyName());
                    salaryLog.setDepartmentId(rs.getString("department_id"));
                    salaryLog.setBaseSalary(baseSalary);
                    salaryLog.setTotalHours(totalClasses);
                    salaryLog.setHourlyPay(hourlyPay);
                    salaryLog.setTotalSalary(baseSalary.add(hourlyPay));

                    salaryLogs.add(salaryLog);
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salaryLogs;
    }

    /**
     * 获取指定教师某月的薪资明细
     */
    public SalaryLogVO getFacultySalary(int facultyId, int month) {
        // 获取指定月份的周数范围
        int[] weekRange = calculateWeekRange(month);
        int startWeek = weekRange[0];
        int endWeek = weekRange[1];

        String sql = "SELECT f.faculty_id, f.faculty_name, f.department_id, r.base_salary, " +
                "COALESCE((" +
                "  SELECT COUNT(*) " +
                "  FROM t_schedule s " +
                "  WHERE s.faculty_id = f.faculty_id " +
                "  AND s.start_week <= ? " + // 使用结束周
                "  AND s.end_week >= ? " + // 使用开始周
                "), 0) as total_classes " +
                "FROM t_faculty f " +
                "JOIN t_role r ON f.role_id = r.role_id " +
                "WHERE f.faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, endWeek); // 结束周
            pstmt.setInt(2, startWeek); // 开始周
            pstmt.setInt(3, facultyId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SalaryLogVO salaryLog = new SalaryLogVO();
                salaryLog.setFacultyId(rs.getInt("faculty_id"));
                salaryLog.setFacultyName(rs.getString("faculty_name"));
                salaryLog.setDepartmentId(rs.getString("department_id"));
                salaryLog.setBaseSalary(rs.getBigDecimal("base_salary"));
                salaryLog.setTotalHours(rs.getInt("total_classes"));

                // 计算课时费（每课时100元）
                BigDecimal hourlyRate = new BigDecimal("100.00");
                BigDecimal hourlyPay = hourlyRate.multiply(new BigDecimal(rs.getInt("total_classes")));
                salaryLog.setHourlyPay(hourlyPay);

                // 计算总工资
                salaryLog.setTotalSalary(rs.getBigDecimal("base_salary").add(hourlyPay));

                return salaryLog;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}