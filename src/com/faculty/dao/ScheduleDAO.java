package com.faculty.dao;

import com.faculty.util.DBUtil;
import com.faculty.vo.ScheduleVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    // 获取所有课程安排
    public List<ScheduleVO> getAllSchedules() {
        List<ScheduleVO> schedules = new ArrayList<>();
        String sql = "SELECT s.*, c.course_name, f.faculty_name, " +
                "GROUP_CONCAT(CASE st.target_type " +
                "WHEN 'class' THEN (SELECT class_name FROM t_class WHERE class_id = st.target_id) " +
                "WHEN 'student' THEN (SELECT student_name FROM t_student WHERE student_id = st.target_id) " +
                "END SEPARATOR ', ') as target_info " +
                "FROM t_schedule s " +
                "LEFT JOIN t_course c ON s.course_id = c.course_id " +
                "LEFT JOIN t_faculty f ON s.faculty_id = f.faculty_id " +
                "LEFT JOIN t_schedule_target st ON s.schedule_id = st.schedule_id " +
                "GROUP BY s.schedule_id " +
                "ORDER BY s.week_day, s.class_time";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ScheduleVO schedule = new ScheduleVO();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setCourseId(rs.getString("course_id"));
                schedule.setCourseName(rs.getString("course_name"));
                schedule.setStartWeek(rs.getInt("start_week"));
                schedule.setEndWeek(rs.getInt("end_week"));
                schedule.setWeekDay(rs.getInt("week_day"));
                schedule.setClassTime(rs.getString("class_time"));
                schedule.setFacultyId(rs.getInt("faculty_id"));
                schedule.setFacultyName(rs.getString("faculty_name"));
                schedule.setLocation(rs.getString("location"));
                schedule.setTargetInfo(rs.getString("target_info"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    // 添加课程安排
    public boolean addSchedule(ScheduleVO schedule, List<String> targetIds, String targetType) {
        String scheduleSql = "INSERT INTO t_schedule (course_id, start_week, end_week, week_day, " +
                "class_time, faculty_id, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String targetSql = "INSERT INTO t_schedule_target (schedule_id, target_type, target_id) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 插入课程安排
                PreparedStatement pstmt = conn.prepareStatement(scheduleSql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, schedule.getCourseId());
                pstmt.setInt(2, schedule.getStartWeek());
                pstmt.setInt(3, schedule.getEndWeek());
                pstmt.setInt(4, schedule.getWeekDay());
                pstmt.setString(5, schedule.getClassTime());
                pstmt.setInt(6, schedule.getFacultyId());
                pstmt.setString(7, schedule.getLocation());
                pstmt.executeUpdate();

                // 获取生成的scheduleId
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int scheduleId = rs.getInt(1);

                    // 插入上课对象
                    PreparedStatement targetPstmt = conn.prepareStatement(targetSql);
                    for (String targetId : targetIds) {
                        targetPstmt.setInt(1, scheduleId);
                        targetPstmt.setString(2, targetType);
                        targetPstmt.setString(3, targetId);
                        targetPstmt.addBatch();
                    }
                    targetPstmt.executeBatch();
                }

                conn.commit();
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

    // 删除课程安排
    public boolean deleteSchedule(int scheduleId) {
        String deleteTargetsSql = "DELETE FROM t_schedule_target WHERE schedule_id = ?";
        String deleteScheduleSql = "DELETE FROM t_schedule WHERE schedule_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 先删除关联的上课对象记录
                try (PreparedStatement pstmt = conn.prepareStatement(deleteTargetsSql)) {
                    pstmt.setInt(1, scheduleId);
                    pstmt.executeUpdate();
                }

                // 再删除课程安排记录
                try (PreparedStatement pstmt = conn.prepareStatement(deleteScheduleSql)) {
                    pstmt.setInt(1, scheduleId);
                    int result = pstmt.executeUpdate();

                    if (result > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
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

    // 检查时间冲突
    public boolean checkTimeConflict(int weekDay, String classTime, List<String> targetIds,
            String targetType, int startWeek, int endWeek, Integer excludeScheduleId) {
        String sql = "SELECT COUNT(*) FROM t_schedule s " +
                "JOIN t_schedule_target st ON s.schedule_id = st.schedule_id " +
                "WHERE s.week_day = ? AND s.class_time = ? " +
                "AND st.target_id IN ('" + String.join("','", targetIds) + "') " +
                "AND st.target_type = ? " +
                "AND NOT (s.end_week < ? OR s.start_week > ?)";

        if (excludeScheduleId != null) {
            sql += " AND s.schedule_id != ?";
        }

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, weekDay);
            pstmt.setString(2, classTime);
            pstmt.setString(3, targetType);
            pstmt.setInt(4, startWeek);
            pstmt.setInt(5, endWeek);

            if (excludeScheduleId != null) {
                pstmt.setInt(6, excludeScheduleId);
            }

            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // 发生错误时返回true表示有冲突
        }
    }

    public boolean batchDeleteSchedules(int[] scheduleIds) {
        String deleteTargetsSql = "DELETE FROM t_schedule_target WHERE schedule_id = ?";
        String deleteScheduleSql = "DELETE FROM t_schedule WHERE schedule_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 先删除关联的上课对象记录
                try (PreparedStatement pstmt = conn.prepareStatement(deleteTargetsSql)) {
                    for (int scheduleId : scheduleIds) {
                        pstmt.setInt(1, scheduleId);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                // 再删除课程安排记录
                try (PreparedStatement pstmt = conn.prepareStatement(deleteScheduleSql)) {
                    for (int scheduleId : scheduleIds) {
                        pstmt.setInt(1, scheduleId);
                        pstmt.addBatch();
                    }
                    int[] results = pstmt.executeBatch();

                    // 检查是否所有删除操作都成功
                    for (int result : results) {
                        if (result <= 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                conn.commit();
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
}