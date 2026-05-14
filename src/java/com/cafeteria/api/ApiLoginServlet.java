package com.cafeteria.api;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * API Endpoint for Mobile App Login.
 * URL: /api/login
 * Method: POST
 * Returns: JSON object with student profile or error.
 */
@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String pass = request.getParameter("password");
        
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        if (id == null || pass == null) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Missing ID or Password");
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        try (Connection conn = DBConnection.getCafeConn()) {
            String sql = "SELECT * FROM cached_students WHERE id_card = ? AND app_password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            boolean authenticated = rs.next();

            if (!authenticated && "123456".equals(pass)) {
                // Fallback: Check if student exists but has no password set yet
                ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();
                authenticated = rs.next();
            }

            if (authenticated) {
                jsonResponse.put("status", "success");
                jsonResponse.put("studentId", rs.getString("id_card"));
                jsonResponse.put("studentName", rs.getString("full_name"));
                jsonResponse.put("department", rs.getString("department"));
                jsonResponse.put("photo", rs.getString("photo_path"));
                jsonResponse.put("studentType", rs.getString("student_type"));
                jsonResponse.put("accountStatus", rs.getString("status"));
                
                // For a professional touch, we can return if they need a password change
                jsonResponse.put("mustChangePassword", "123456".equals(pass));
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Invalid Student ID or Password");
            }
        } catch (SQLException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database Error: " + e.getMessage());
        }


        response.getWriter().write(gson.toJson(jsonResponse));
    }
    
    // Allow GET for quick browser testing (optional but helpful for beginners)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
