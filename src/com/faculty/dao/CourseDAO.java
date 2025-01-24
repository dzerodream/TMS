package com.faculty.dao;

import com.faculty.model.Course;
import com.faculty.util.DBUtil;
import com.faculty.vo.CourseVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // 获取所有课程信息（包括院系名称）
    public List<CourseVO> getAllCourses() {
        List<CourseVO> courses = new ArrayList<>();
        String sql = "SELECT c.*, d.dept_name " +
                "FROM t_course c " +
                "LEFT JOIN t_department d ON c.dept_id = d.dept_id " +
                "ORDER BY c.course_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CourseVO course = new CourseVO();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setDeptId(rs.getString("dept_id"));
                course.setDepartmentName(rs.getString("dept_name"));
                course.setCredits(rs.getBigDecimal("credits"));
                course.setTotalHours(rs.getInt("total_hours"));
                course.setCourseNature(rs.getString("course_nature"));
                course.setAssessmentMethod(rs.getString("assessment_method"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // 添加课程
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO t_course (course_id, course_name, dept_id, credits, " +
                "total_hours, course_nature, assessment_method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDeptId());
            pstmt.setBigDecimal(4, course.getCredits());
            pstmt.setInt(5, course.getTotalHours());
            pstmt.setString(6, course.getCourseNature());
            pstmt.setString(7, course.getAssessmentMethod());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除课程
    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM t_course WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查课程是否已被安排课表
    public boolean hasSchedule(String courseId) {
        String sql = "SELECT COUNT(*) FROM t_schedule WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 批量删除课程
    public boolean batchDeleteCourses(String[] courseIds) {
        String sql = "DELETE FROM t_course WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            try {
                for (String courseId : courseIds) {
                    pstmt.setString(1, courseId);
                    pstmt.addBatch();
                }

                int[] results = pstmt.executeBatch();
                conn.commit();

                // 检查是否所有删除操作都成功
                for (int result : results) {
                    if (result <= 0) {
                        return false;
                    }
                }
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取课程详情
    public Course getCourseById(String courseId) {
        String sql = "SELECT * FROM t_course WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setDeptId(rs.getString("dept_id"));
                course.setCredits(rs.getBigDecimal("credits"));
                course.setTotalHours(rs.getInt("total_hours"));
                course.setCourseNature(rs.getString("course_nature"));
                course.setAssessmentMethod(rs.getString("assessment_method"));
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新课程
    public boolean updateCourse(Course course) {
        String sql = "UPDATE t_course SET course_name = ?, dept_id = ?, credits = ?, " +
                "total_hours = ?, course_nature = ?, assessment_method = ? " +
                "WHERE course_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getDeptId());
            pstmt.setBigDecimal(3, course.getCredits());
            pstmt.setInt(4, course.getTotalHours());
            pstmt.setString(5, course.getCourseNature());
            pstmt.setString(6, course.getAssessmentMethod());
            pstmt.setString(7, course.getCourseId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取课程数量
    public int getCourseCount() {
        String sql = "SELECT COUNT(*) FROM t_course";

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

    /**
     * 获取教师的所有课程
     */
    public List<Course> getTeacherCourses(int facultyId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT DISTINCT c.course_id, c.course_name, c.credits " +
                "FROM t_course c " +
                "JOIN t_schedule s ON c.course_id = s.course_id " +
                "WHERE s.faculty_id = ? " +
                "ORDER BY c.course_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCredits(rs.getBigDecimal("credits"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}