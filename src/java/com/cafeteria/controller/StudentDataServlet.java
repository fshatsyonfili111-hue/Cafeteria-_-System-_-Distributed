package com.cafeteria.controller;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/student-info")
public class StudentDataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement("SELECT full_name, photo_path, department, status, student_type, age FROM cached_students WHERE id_card = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", rs.getString("full_name"));
                data.put("photo", rs.getString("photo_path"));
                data.put("dept", rs.getString("department"));
                data.put("status", rs.getString("status"));
                data.put("type", rs.getString("student_type"));
                data.put("age", rs.getInt("age"));
                response.getWriter().write(new Gson().toJson(data));
            } else {
                response.getWriter().write("{\"error\":\"Not found\"}");
            }
        } catch (SQLException e) {
            response.getWriter().write("{\"error\":\"Database error\"}");
        }
    }
}