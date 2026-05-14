package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sql = "INSERT INTO feedback (id_card, meal_type, rating, comment, feedback_date) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getCafeConn(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, request.getParameter("id"));
            ps.setString(2, request.getParameter("mealType"));
            ps.setInt(3, Integer.parseInt(request.getParameter("rating")));
            ps.setString(4, request.getParameter("comment"));
            ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            
            ps.executeUpdate();
            response.sendRedirect("student_menu_view.html?id="+request.getParameter("id")+"&status=feedback_success");
        } catch (Exception e) { 
            response.getWriter().write("Error: " + e.getMessage() + ". Please ensure 'feedback_date' column exists in 'feedback' table."); 
        }
    }
}