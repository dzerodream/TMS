package com.faculty.dao;

import com.faculty.model.Faculty;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FacultyDAO {
    public Faculty validateLogin(int facultyId, String password) {
        String sql = "SELECT * FROM t_faculty WHERE faculty_id = ? AND password = ? AND status = 1";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setFacultyId(rs.getInt("faculty_id"));
                    faculty.setFacultyName(rs.getString("faculty_name"));
                    faculty.setDepartmentId(rs.getString("department_id"));
                    faculty.setRoleId(rs.getInt("role_id"));
                    faculty.setPhoneNumber(rs.getString("phone_number"));
                    faculty.setIdNumber(rs.getString("id_number"));
                    faculty.setStatus(rs.getInt("status"));
                    faculty.setHireDate(rs.getDate("hire_date"));
                    faculty.setLeaveDate(rs.getDate("leave_date"));
                    return faculty;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Faculty> getAllFaculty() {
        List<Faculty> facultyList = new ArrayList<>();
        String sql = "SELECT f.*, d.dept_name as department_name " +
                "FROM t_faculty f " +
                "LEFT JOIN t_department d ON f.department_id = d.dept_id " +
                "WHERE f.status = 1 " + // 只查询在职员工
                "ORDER BY f.faculty_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setDepartmentId(rs.getString("department_id"));
                faculty.setDepartmentName(rs.getString("department_name"));
                faculty.setRoleId(rs.getInt("role_id"));
                faculty.setPhoneNumber(rs.getString("phone_number"));
                faculty.setStatus(rs.getInt("status"));
                faculty.setHireDate(rs.getDate("hire_date"));
                faculty.setLeaveDate(rs.getDate("leave_date"));
                facultyList.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查询员工列表出错: " + e.getMessage());
        }
        return facultyList;
    }

    public boolean addFaculty(Faculty faculty) {
        String sql = "INSERT INTO t_faculty (faculty_name, department_id, role_id, phone_number, " +
                "id_number, status, hire_date, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置默认值
            faculty.setStatus(1); // 1表示在职
            faculty.setHireDate(Date.valueOf(LocalDate.now())); // 使用 java.sql.Date
            if (faculty.getRoleId() == 0) {
                faculty.setRoleId(3); // 默认为普通教职工
            }

            pstmt.setString(1, faculty.getFacultyName());
            pstmt.setString(2, faculty.getDepartmentId());
            pstmt.setInt(3, faculty.getRoleId());
            pstmt.setString(4, faculty.getPhoneNumber());
            pstmt.setString(5, faculty.getIdNumber());
            pstmt.setInt(6, faculty.getStatus());
            pstmt.setDate(7, faculty.getHireDate());
            pstmt.setString(8, faculty.getPassword());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("添加教职工失败: " + e.getMessage());
            return false;
        }
    }

    public boolean setRole(int facultyId, int roleId) {
        String sql = "UPDATE t_faculty SET role_id = ? WHERE faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roleId);
            pstmt.setInt(2, facultyId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("设置权限失败: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFaculty(int facultyId) {
        String sql = "UPDATE t_faculty SET status = 0, leave_date = CURRENT_DATE WHERE faculty_id = ?";
        System.out.println("执行删除SQL: " + sql + ", facultyId = " + facultyId);

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            int result = pstmt.executeUpdate();
            System.out.println("SQL执行结果: " + result);

            return result > 0;

        } catch (SQLException e) {
            System.out.println("删除成员时发生SQL错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean batchDeleteFaculty(List<Integer> facultyIds) {
        String sql = "UPDATE t_faculty SET status = 0, leave_date = CURRENT_DATE WHERE faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (Integer facultyId : facultyIds) {
                pstmt.setInt(1, facultyId);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit();

            // 检查是否所有操作都成功
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("批量删除成员失败: " + e.getMessage());
            return false;
        }
    }

    public Faculty getFacultyById(int facultyId) {
        String sql = "SELECT f.*, d.dept_name as department_name " +
                "FROM t_faculty f " +
                "LEFT JOIN t_department d ON f.department_id = d.dept_id " +
                "WHERE f.faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setDepartmentId(rs.getString("department_id"));
                faculty.setDepartmentName(rs.getString("department_name"));
                faculty.setRoleId(rs.getInt("role_id"));
                faculty.setPhoneNumber(rs.getString("phone_number"));
                faculty.setIdNumber(rs.getString("id_number"));
                faculty.setStatus(rs.getInt("status"));
                faculty.setHireDate(rs.getDate("hire_date"));
                faculty.setLeaveDate(rs.getDate("leave_date"));
                return faculty;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateFaculty(Faculty faculty) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_faculty SET faculty_name = ?, department_id = ?, ");
        sql.append("phone_number = ?, id_number = ?, role_id = ?");

        // 只有当密码不为空时才更新密码
        if (faculty.getPassword() != null && !faculty.getPassword().trim().isEmpty()) {
            sql.append(", password = ?");
        }

        sql.append(" WHERE faculty_id = ?");

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            pstmt.setString(paramIndex++, faculty.getFacultyName());
            pstmt.setString(paramIndex++, faculty.getDepartmentId());
            pstmt.setString(paramIndex++, faculty.getPhoneNumber());
            pstmt.setString(paramIndex++, faculty.getIdNumber());
            pstmt.setInt(paramIndex++, faculty.getRoleId());

            // 只有当密码不为空时才设置密码参数
            if (faculty.getPassword() != null && !faculty.getPassword().trim().isEmpty()) {
                pstmt.setString(paramIndex++, faculty.getPassword());
            }

            pstmt.setInt(paramIndex, faculty.getFacultyId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Faculty> getRetiredFaculty() {
        List<Faculty> facultyList = new ArrayList<>();
        String sql = "SELECT f.*, d.dept_name as department_name, " +
                "DATEDIFF(CURRENT_DATE, f.leave_date) as leave_days " +
                "FROM t_faculty f " +
                "LEFT JOIN t_department d ON f.department_id = d.dept_id " +
                "WHERE f.status = 0 " +
                "ORDER BY f.leave_date DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setDepartmentId(rs.getString("department_id"));
                faculty.setDepartmentName(rs.getString("department_name"));
                faculty.setPhoneNumber(rs.getString("phone_number"));
                faculty.setStatus(rs.getInt("status"));
                faculty.setHireDate(rs.getDate("hire_date"));
                faculty.setLeaveDate(rs.getDate("leave_date"));
                faculty.setLeaveDays(rs.getLong("leave_days"));
                facultyList.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList;
    }

    public boolean restoreFaculty(int facultyId) {
        String sql = "UPDATE t_faculty SET status = 1, leave_date = NULL WHERE faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRetiredFaculty(int facultyId) {
        // 首先检查是否为离职状态
        String checkSql = "SELECT status FROM t_faculty WHERE faculty_id = ?";
        String deleteSql = "DELETE FROM t_faculty WHERE faculty_id = ? AND status = 0";

        try (Connection conn = DBUtil.getConnection()) {
            // 检查是否为离职状态
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, facultyId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt("status") != 0) {
                    return false; // 不是离职状态，不能删除
                }
            }

            // 执行删除
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, facultyId);
                return deleteStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("删除离职员工失败: " + e.getMessage());
            return false;
        }
    }

    // 批量删除离职员工
    public boolean batchDeleteRetiredFaculty(List<Integer> facultyIds) {
        String sql = "DELETE FROM t_faculty WHERE faculty_id = ? AND status = 0";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (Integer facultyId : facultyIds) {
                pstmt.setInt(1, facultyId);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit();

            // 检查是否所有操作都成功
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有在职教职工
    public List<Faculty> getAllActiveFaculty() {
        List<Faculty> facultyList = new ArrayList<>();
        String sql = "SELECT f.*, d.dept_name as department_name " +
                "FROM t_faculty f " +
                "LEFT JOIN t_department d ON f.department_id = d.dept_id " +
                "WHERE f.status = 1 " +
                "ORDER BY f.faculty_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setDepartmentId(rs.getString("department_id"));
                faculty.setDepartmentName(rs.getString("department_name"));
                faculty.setRoleId(rs.getInt("role_id"));
                faculty.setPhoneNumber(rs.getString("phone_number"));
                faculty.setIdNumber(rs.getString("id_number"));
                faculty.setStatus(rs.getInt("status"));
                faculty.setHireDate(rs.getDate("hire_date"));
                faculty.setLeaveDate(rs.getDate("leave_date"));
                facultyList.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList;
    }

    public int getActiveFacultyCount() {
        String sql = "SELECT COUNT(*) FROM t_faculty WHERE status = 1";

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

    // 获取指定院系的教师列表
    public List<Faculty> getFacultyByDepartment(String departmentId) {
        List<Faculty> facultyList = new ArrayList<>();
        String sql = "SELECT f.*, d.dept_name as department_name " +
                "FROM t_faculty f " +
                "LEFT JOIN t_department d ON f.department_id = d.dept_id " +
                "WHERE f.department_id = ? AND f.status = 1 " +
                "ORDER BY f.faculty_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setFacultyName(rs.getString("faculty_name"));
                faculty.setDepartmentId(rs.getString("department_id"));
                faculty.setDepartmentName(rs.getString("department_name"));
                faculty.setRoleId(rs.getInt("role_id"));
                faculty.setPhoneNumber(rs.getString("phone_number"));
                faculty.setIdNumber(rs.getString("id_number"));
                faculty.setStatus(rs.getInt("status"));
                faculty.setHireDate(rs.getDate("hire_date"));
                facultyList.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList;
    }

    /**
     * 更新教师状态
     * 
     * @param facultyId 教师ID
     * @param status    状态值（0:离职 1:在职）
     * @return 更新是否成功
     */
    public boolean updateFacultyStatus(int facultyId, int status) {
        String sql = "UPDATE t_faculty SET status = ?, leave_date = ? WHERE faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, status);
            // 如果是设置离职状态，则设置离职日期
            if (status == 0) {
                pstmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            } else {
                pstmt.setNull(2, java.sql.Types.DATE);
            }
            pstmt.setInt(3, facultyId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新教师个人信息
     */
    public boolean updateFacultyProfile(Faculty faculty) {
        String sql = "UPDATE t_faculty SET phone_number = ?, id_number = ? ";
        if (faculty.getPassword() != null && !faculty.getPassword().trim().isEmpty()) {
            sql += ", password = ? ";
        }
        sql += "WHERE faculty_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Updating faculty: " + faculty.getFacultyId()); // 调试输出

            pstmt.setString(1, faculty.getPhoneNumber());
            pstmt.setString(2, faculty.getIdNumber());

            int paramIndex = 3;
            if (faculty.getPassword() != null && !faculty.getPassword().trim().isEmpty()) {
                pstmt.setString(paramIndex++, faculty.getPassword());
            }
            pstmt.setInt(paramIndex, faculty.getFacultyId());

            int result = pstmt.executeUpdate();
            System.out.println("Update result: " + result); // 调试输出
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateFacultyProfile: " + e.getMessage()); // 错误日志
            e.printStackTrace();
            return false;
        }
    }
}