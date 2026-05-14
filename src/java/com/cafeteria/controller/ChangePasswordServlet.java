package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        if (!newPass.equals(confirm)) {
            response.getWriter().write("Passwords do not match!");
            return;
        }

        String sql = "UPDATE cached_students SET app_password = ? WHERE id_card = ? AND app_password = ?";
        
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPass);
            ps.setString(2, id);
            ps.setString(3, oldPass);
            
            int updated = ps.executeUpdate();
            if (updated > 0) {
                response.sendRedirect("student_menu_view.html");
            } else {
                response.getWriter().write("Old password incorrect.");
            }
        } catch (Exception e) { response.getWriter().write("Error: " + e.getMessage()); }
    }
}