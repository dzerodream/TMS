package com.faculty.dao;

import com.faculty.model.Department;
import com.faculty.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // 获取所有院系信息
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT d.*, f.faculty_name as admin_name " +
                "FROM t_department d " +
                "LEFT JOIN t_faculty f ON f.department_id = d.dept_id " +
                "AND f.role_id = 2 " + // role_id = 2 表示院系管理员
                "ORDER BY d.dept_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Department dept = new Department();
                dept.setDeptId(rs.getString("dept_id"));
                dept.setDeptName(rs.getString("dept_name"));
                dept.setNumOfPeople(rs.getInt("num_of_people"));
                dept.setAdminName(rs.getString("admin_name"));
                departments.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    // 添加院系
    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO t_department (dept_id, dept_name) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.getDeptId());
            pstmt.setString(2, department.getDeptName());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新院系信息
    public boolean updateDepartment(Department department) {
        String sql = "UPDATE t_department SET dept_name = ? WHERE dept_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.getDeptName());
            pstmt.setString(2, department.getDeptId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除院系
    public boolean deleteDepartment(String deptId) {
        // 首先检查是否有关联的教职工或学生
        String checkFacultySql = "SELECT COUNT(*) FROM t_faculty WHERE department_id = ?";
        String checkStudentSql = "SELECT COUNT(*) FROM t_student WHERE department_id = ?";
        String deleteSql = "DELETE FROM t_department WHERE dept_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            // 检查是否有教职工
            try (PreparedStatement checkStmt = conn.prepareStatement(checkFacultySql)) {
                checkStmt.setString(1, deptId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // 有关联的教职工，不能删除
                }
            }

            // 检查是否有学生
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStudentSql)) {
                checkStmt.setString(1, deptId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // 有关联的学生，不能删除
                }
            }

            // 执行删除
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, deptId);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据ID获取院系详情
    public Department getDepartmentById(String deptId) {
        String sql = "SELECT * FROM t_department WHERE dept_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deptId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Department dept = new Department();
                dept.setDeptId(rs.getString("dept_id"));
                dept.setDeptName(rs.getString("dept_name"));
                dept.setNumOfPeople(rs.getInt("num_of_people"));
                return dept;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新所有院系的教职工人数
     */
    public boolean updateAllDepartmentPeopleCount() {
        String sql = "UPDATE t_department d SET num_of_people = (" +
                "SELECT COUNT(*) FROM t_faculty f " +
                "WHERE f.department_id = d.dept_id AND f.status = 1)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDepartmentCount() {
        String sql = "SELECT COUNT(*) FROM t_department";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}