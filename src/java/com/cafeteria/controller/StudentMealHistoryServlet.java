package com.cafeteria.controller;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/meal-history")
public class StudentMealHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<Map<String, String>> history = new ArrayList<>();
        
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement("SELECT meal_type, meal_date FROM meal_records WHERE id_card = ? ORDER BY meal_date DESC LIMIT 20")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("meal", rs.getString("meal_type"));
                record.put("date", rs.getDate("meal_date").toString());
                history.add(record);
            }
            
            response.getWriter().write(new Gson().toJson(history));
            
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
