package com.cafeteria.api;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/menu")
public class MenuServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Check if the user wants "all" or just "today"
        String scope = request.getParameter("scope"); 
        String sql;
        
        if ("all".equals(scope)) {
            // Get everything ordered by day
            sql = "SELECT * FROM menu_schedule ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";
        } else {
            // Get only today
            sql = "SELECT * FROM menu_schedule WHERE day_of_week = DAYNAME(CURDATE())";
        }
        
        List<Map<String, String>> menuList = new ArrayList<>();
        try (Connection conn = DBConnection.getCafeConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> item = new HashMap<>();
                item.put("day", rs.getString("day_of_week"));
                item.put("type", rs.getString("meal_type"));
                item.put("photo", rs.getString("menu_photo"));
                item.put("desc", rs.getString("description"));
                item.put("time", rs.getString("start_time") + " - " + rs.getString("end_time"));
                menuList.add(item);
            }
            response.getWriter().write(new Gson().toJson(menuList));
        } catch (SQLException e) { response.getWriter().write("[]"); }
    }
}