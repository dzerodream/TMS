package com.faculty.dao;

import com.faculty.model.Student;
import com.faculty.model.ClassInfo;
import com.faculty.util.DBUtil;
import com.faculty.vo.StudentVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 获取所有学生
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, c.class_name " +
                "FROM t_student s " +
                "LEFT JOIN t_department d ON s.department_id = d.dept_id " +
                "LEFT JOIN t_class c ON s.class_id = c.class_id " +
                "ORDER BY s.student_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setStudentName(rs.getString("student_name"));
                student.setGender(rs.getString("gender"));
                student.setEnrollmentYear(rs.getInt("enrollment_year"));
                student.setPhoneNumber(rs.getString("phone_number"));
                student.setIdNumber(rs.getString("id_number"));
                student.setStatus(rs.getInt("status"));
                student.setDepartmentId(rs.getString("department_id"));
                student.setDepartmentName(rs.getString("dept_name"));
                student.setClassId(rs.getString("class_id"));
                student.setClassName(rs.getString("class_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // 添加学生
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO t_student (student_id, student_name, gender, enrollment_year, " +
                "phone_number, id_number, status, department_id, class_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getStudentName());
            pstmt.setString(3, student.getGender());
            pstmt.setInt(4, student.getEnrollmentYear());
            pstmt.setString(5, student.getPhoneNumber());
            pstmt.setString(6, student.getIdNumber());
            pstmt.setInt(7, student.getStatus());
            pstmt.setString(8, student.getDepartmentId());
            pstmt.setString(9, student.getClassId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除学生
    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM t_student WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 批量删除学生
    public boolean batchDeleteStudents(List<String> studentIds) {
        String sql = "DELETE FROM t_student WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Executing batch delete SQL: " + sql);
            System.out.println("With student IDs: " + studentIds);

            conn.setAutoCommit(false);
            for (String studentId : studentIds) {
                pstmt.setString(1, studentId);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit();

            // 检查每个删除的结果
            for (int i = 0; i < results.length; i++) {
                if (results[i] <= 0) {
                    System.out.println("Failed to delete student ID: " + studentIds.get(i));
                    return false;
                }
            }

            System.out.println("Batch delete successful");
            return true;
        } catch (SQLException e) {
            System.out.println("Batch delete error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有班级
    public List<ClassInfo> getAllClasses() {
        List<ClassInfo> classes = new ArrayList<>();
        String sql = "SELECT class_id, class_name FROM t_class ORDER BY class_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassId(rs.getString("class_id"));
                classInfo.setClassName(rs.getString("class_name"));
                classes.add(classInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // 获取学生详情
    public Student getStudentById(String studentId) {
        String sql = "SELECT s.*, d.dept_name, c.class_name " +
                "FROM t_student s " +
                "LEFT JOIN t_department d ON s.department_id = d.dept_id " +
                "LEFT JOIN t_class c ON s.class_id = c.class_id " +
                "WHERE s.student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setStudentName(rs.getString("student_name"));
                student.setGender(rs.getString("gender"));
                student.setEnrollmentYear(rs.getInt("enrollment_year"));
                student.setPhoneNumber(rs.getString("phone_number"));
                student.setIdNumber(rs.getString("id_number"));
                student.setStatus(rs.getInt("status"));
                student.setDepartmentId(rs.getString("department_id"));
                student.setDepartmentName(rs.getString("dept_name"));
                student.setClassId(rs.getString("class_id"));
                student.setClassName(rs.getString("class_name"));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新学生信息
    public boolean updateStudent(Student student) {
        String sql = "UPDATE t_student SET student_name = ?, gender = ?, " +
                "phone_number = ?, id_number = ?, department_id = ?, class_id = ? " +
                "WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentName());
            pstmt.setString(2, student.getGender());
            pstmt.setString(3, student.getPhoneNumber());
            pstmt.setString(4, student.getIdNumber());
            pstmt.setString(5, student.getDepartmentId());
            pstmt.setString(6, student.getClassId());
            pstmt.setString(7, student.getStudentId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新学生状态
    public boolean updateStudentStatus(String studentId, int status) {
        String sql = "UPDATE t_student SET status = ? WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Executing update status SQL: " + sql);
            System.out.println("Parameters: [status=" + status + ", studentId=" + studentId + "]");

            pstmt.setInt(1, status);
            pstmt.setString(2, studentId);

            int result = pstmt.executeUpdate();
            System.out.println("Update status result: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Update status error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 根据院系ID获取班级列表
    public List<ClassInfo> getClassesByDepartment(String departmentId) {
        List<ClassInfo> classes = new ArrayList<>();
        String sql = "SELECT class_id, class_name FROM t_class WHERE department_id = ? ORDER BY class_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassId(rs.getString("class_id"));
                classInfo.setClassName(rs.getString("class_name"));
                classInfo.setDepartmentId(departmentId);
                classes.add(classInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // 获取活跃学生数量
    public int getActiveStudentCount() {
        String sql = "SELECT COUNT(*) FROM t_student WHERE status = 1";

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

    // 获取指定院系的学生列表
    public List<Student> getStudentsByDepartment(String departmentId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, c.class_name " +
                "FROM t_student s " +
                "LEFT JOIN t_department d ON s.department_id = d.dept_id " +
                "LEFT JOIN t_class c ON s.class_id = c.class_id " +
                "WHERE s.department_id = ? " +
                "ORDER BY s.student_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setStudentName(rs.getString("student_name"));
                student.setGender(rs.getString("gender"));
                student.setEnrollmentYear(rs.getInt("enrollment_year"));
                student.setPhoneNumber(rs.getString("phone_number"));
                student.setIdNumber(rs.getString("id_number"));
                student.setStatus(rs.getInt("status"));
                student.setDepartmentId(rs.getString("department_id"));
                student.setDepartmentName(rs.getString("dept_name"));
                student.setClassId(rs.getString("class_id"));
                student.setClassName(rs.getString("class_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // 获取下一个学号序列
    public int getNextSequence(String departmentId) {
        String sql = "SELECT MAX(CAST(SUBSTRING(student_id, 5) AS UNSIGNED)) " +
                "FROM t_student WHERE department_id = ? AND " +
                "SUBSTRING(student_id, 1, 4) = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String year = String.valueOf(java.time.Year.now().getValue()).substring(2);
            String prefix = year + departmentId.substring(0, 2);

            pstmt.setString(1, departmentId);
            pstmt.setString(2, prefix);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int maxSeq = rs.getInt(1);
                return maxSeq + 1;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取指定课程的学生名单
     */
    public List<StudentVO> getCourseStudents(String courseId) {
        List<StudentVO> students = new ArrayList<>();
        String sql = "SELECT DISTINCT s.student_id, s.student_name, s.phone_number, " +
                "c.class_name " +
                "FROM t_student s " +
                "JOIN t_class c ON s.class_id = c.class_id " +
                "JOIN t_schedule_target st ON " +
                "(st.target_type = 'student' AND st.target_id = s.student_id) OR " +
                "(st.target_type = 'class' AND st.target_id = s.class_id) " +
                "JOIN t_schedule sch ON st.schedule_id = sch.schedule_id " +
                "WHERE sch.course_id = ? " +
                "ORDER BY s.student_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentVO student = new StudentVO();
                student.setStudentId(rs.getString("student_id"));
                student.setStudentName(rs.getString("student_name"));
                student.setClassName(rs.getString("class_name"));
                student.setPhoneNumber(rs.getString("phone_number"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}