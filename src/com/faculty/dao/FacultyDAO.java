package com.faculty.dao;

import com.faculty.model.Faculty;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}