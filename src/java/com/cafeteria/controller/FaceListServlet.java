package com.cafeteria.controller;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/face-list")
public class FaceListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<Map<String, String>> faceTemplates = new ArrayList<>();
        
        String sql = "SELECT id_card, encoding_data FROM face_templates";
        
        try (Connection conn = DBConnection.getCafeConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> entry = new HashMap<>();
                entry.put("id", rs.getString("id_card"));
                entry.put("encoding", rs.getString("encoding_data"));
                faceTemplates.add(entry);
            }
            
            response.getWriter().write(new Gson().toJson(faceTemplates));
            
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
