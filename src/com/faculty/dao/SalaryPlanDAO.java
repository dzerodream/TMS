package com.faculty.dao;

import com.faculty.util.DBUtil;
import com.faculty.vo.SalaryPlanVO;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryPlanDAO {

    // 获取所有角色的薪酬计划
    public List<SalaryPlanVO> getAllSalaryPlans() {
        List<SalaryPlanVO> plans = new ArrayList<>();
        String sql = "SELECT r.role_id, r.role_name, r.base_salary, " +
                "(SELECT COUNT(*) FROM t_faculty f WHERE f.role_id = r.role_id AND f.status = 1) as faculty_count " +
                "FROM t_role r ORDER BY r.role_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SalaryPlanVO plan = new SalaryPlanVO();
                plan.setRoleId(rs.getInt("role_id"));
                plan.setRoleName(rs.getString("role_name"));
                plan.setBaseSalary(rs.getBigDecimal("base_salary"));
                plan.setFacultyCount(rs.getInt("faculty_count"));
                plans.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    // 更新角色基本工资
    public boolean updateBaseSalary(int roleId, BigDecimal baseSalary) {
        String sql = "UPDATE t_role SET base_salary = ? WHERE role_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, baseSalary);
            pstmt.setInt(2, roleId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}