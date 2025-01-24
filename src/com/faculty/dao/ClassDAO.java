package com.faculty.dao;

import com.faculty.model.ClassInfo;
import com.faculty.util.DBUtil;
import com.faculty.vo.ClassVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    // 获取所有班级信息（包括院系名称和学生人数）
    public List<ClassVO> getAllClasses() {
        List<ClassVO> classes = new ArrayList<>();
        String sql = "SELECT c.*, d.dept_name, " +
                "(SELECT COUNT(*) FROM t_student s WHERE s.class_id = c.class_id) as student_count " +
                "FROM t_class c " +
                "LEFT JOIN t_department d ON c.department_id = d.dept_id " +
                "ORDER BY c.class_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ClassVO classVO = new ClassVO();
                classVO.setClassId(rs.getString("class_id"));
                classVO.setClassName(rs.getString("class_name"));
                classVO.setDepartmentId(rs.getString("department_id"));
                classVO.setDepartmentName(rs.getString("dept_name"));
                classVO.setStudentCount(rs.getInt("student_count"));
                classes.add(classVO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // 添加班级
    public boolean addClass(ClassInfo classInfo) {
        String sql = "INSERT INTO t_class (class_id, class_name, department_id) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, classInfo.getClassId());
            pstmt.setString(2, classInfo.getClassName());
            pstmt.setString(3, classInfo.getDepartmentId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除班级
    public boolean deleteClass(String classId) {
        String sql = "DELETE FROM t_class WHERE class_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, classId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查班级是否有学生
    public boolean hasStudents(String classId) {
        String sql = "SELECT COUNT(*) FROM t_student WHERE class_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, classId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}