package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/FaceRegistrationServlet")
public class FaceRegistrationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("id_card");
        String encodingData = request.getParameter("encoding");

        if (studentId == null || encodingData == null) {
            response.getWriter().write("Error: Missing Data");
            return;
        }

        String sql = "INSERT INTO face_templates (id_card, encoding_data) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE encoding_data = ?";

        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, encodingData);
            ps.setString(3, encodingData);
            ps.executeUpdate();
            
            response.setContentType("text/plain");
            response.getWriter().print("Success");
        } catch (SQLException e) {
            response.getWriter().write("Database Error: " + e.getMessage());
        }
    }
}
