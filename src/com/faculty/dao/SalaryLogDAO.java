package com.faculty.dao;

import com.faculty.model.Faculty;
import com.faculty.vo.SalaryLogVO;
import com.faculty.util.DBUtil;

import java.sql.*;
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
                    double baseSalary = rs.getDouble("base_salary");
                    double hourlyPay = totalClasses * 100.0; // 课时费=课时数*100

                    SalaryLogVO salaryLog = new SalaryLogVO();
                    salaryLog.setFacultyId(faculty.getFacultyId());
                    salaryLog.setFacultyName(faculty.getFacultyName());
                    salaryLog.setDepartmentName(faculty.getDepartmentName());
                    salaryLog.setBaseSalary(baseSalary);
                    salaryLog.setTotalHours(totalClasses);
                    salaryLog.setHourlyPay(hourlyPay);
                    salaryLog.setTotalSalary(baseSalary + hourlyPay);

                    salaryLogs.add(salaryLog);
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salaryLogs;
    }
}