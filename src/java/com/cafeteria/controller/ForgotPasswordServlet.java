package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        
        // Reset password to default '123456'
        String sql = "UPDATE cached_students SET app_password = '123456' WHERE id_card = ?";
        
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                response.getWriter().write("Password reset to 123456. Please <a href='login.html'>Login here</a>.");
            } else {
                response.getWriter().write("ID not found.");
            }
        } catch (SQLException e) { response.getWriter().write("Error: " + e.getMessage()); }
    }
}