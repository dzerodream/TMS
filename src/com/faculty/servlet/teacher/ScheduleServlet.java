package com.faculty.servlet.teacher;

import com.faculty.dao.ScheduleDAO;
import com.faculty.model.Faculty;
import com.faculty.vo.ScheduleVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/teacher/schedule")
public class ScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Faculty faculty = (Faculty) request.getSession().getAttribute("faculty");
        if (faculty == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 获取当前周
        int currentWeek;
        String weekParam = request.getParameter("week");
        try {
            if (weekParam != null && !weekParam.trim().isEmpty()) {
                currentWeek = Integer.parseInt(weekParam);
                // 验证周数范围
                if (currentWeek < 1 || currentWeek > 16) {
                    currentWeek = getCurrentWeek();
                }
            } else {
                currentWeek = getCurrentWeek();
            }
        } catch (NumberFormatException e) {
            currentWeek = getCurrentWeek();
        }

        System.out.println("Current Week: " + currentWeek); // 调试输出

        // 获取教师的课程安排
        List<ScheduleVO> schedules = scheduleDAO.getTeacherSchedules(faculty.getFacultyId(), currentWeek);
        System.out.println("Found " + schedules.size() + " schedules"); // 调试输出

        // 将课程安排转换为二维表格数据
        Map<Integer, Map<Integer, List<ScheduleVO>>> scheduleMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            scheduleMap.put(i, new HashMap<>());
            for (int j = 1; j <= 5; j++) {
                scheduleMap.get(i).put(j, new ArrayList<>());
            }
        }

        // 填充课程数据
        for (ScheduleVO schedule : schedules) {
            int weekDay = schedule.getWeekDay();
            int classTime = Integer.parseInt(schedule.getClassTime());
            System.out.println("Adding schedule: Day " + weekDay + ", Time " + classTime); // 调试输出
            scheduleMap.get(weekDay).get(classTime).add(schedule);
        }

        request.setAttribute("currentWeek", currentWeek);
        request.setAttribute("scheduleMap", scheduleMap);

        request.getRequestDispatcher("/teacher/schedule.jsp").forward(request, response);
    }

    private int getCurrentWeek() {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 9, 1);
        LocalDate now = LocalDate.now();
        int weeksBetween = (int) ((now.toEpochDay() - startDate.toEpochDay()) / 7) + 1;
        int currentWeek = Math.min(Math.max(weeksBetween, 1), 16);
        System.out.println("Calculated current week: " + currentWeek); // 调试输出
        return currentWeek;
    }
}