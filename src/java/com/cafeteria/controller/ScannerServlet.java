package com.cafeteria.controller;

import com.cafeteria.db.MealManager;
import com.campus.db.DBConnection;
import com.campus.model.Student;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ScannerServlet")
public class ScannerServlet extends HttpServlet {

    private String getCurrentMeal() {
        // Query database for today's day (e.g., 'Saturday') and current time
        String sql = "SELECT meal_type FROM menu_schedule WHERE day_of_week = DAYNAME(CURDATE()) AND CURTIME() BETWEEN start_time AND end_time";
        try (Connection conn = DBConnection.getCafeConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getString("meal_type");
        } catch (SQLException e) { e.printStackTrace(); }
        return null; 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        String studentId = rawId.contains("UniqueCode") ? rawId.split("UniqueCode")[1].split(",")[0] : rawId;

        try {
            Student s = MealManager.getStudent(studentId);
            if (s == null) {
                response.getWriter().write("DENIED: ID Not Found.|Unknown|images/default.jpg|N/A|0|N/A");
                return;
            }

            String fullInfo = "|" + s.firstName + " " + s.lastName + "|" + s.photoPath + "|" + s.dept + "|" + studentId;

            if (!"Active".equalsIgnoreCase(s.status)) {
                response.getWriter().write("DENIED: INACTIVE" + fullInfo);
            } else if ("Non-Cafe".equalsIgnoreCase(s.studentType)) {
                response.getWriter().write("DENIED: NON-CAFE" + fullInfo);
            } else {
                String meal = getCurrentMeal();
                if (meal == null) {
                    response.getWriter().write("DENIED: Not meal time." + fullInfo);
                } else if (MealManager.hasAlreadyEaten(studentId, meal)) {
                    response.getWriter().write("DENIED: Already ate " + meal + "." + fullInfo);
                } else {
                    MealManager.logMeal(studentId, meal);
                    response.getWriter().write("GRANTED" + fullInfo);
                }
            }
        } catch (SQLException e) { response.getWriter().write("ERROR: " + e.getMessage()); }
    }
}