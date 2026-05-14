package com.cafeteria.controller;

import com.campus.db.DBConnection;
import com.campus.service.SyncService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/RegistrarDashboard")
public class RegistrarDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String status = request.getParameter("status");

        if ("toggle".equals(action)) {
            toggleStatus(id, status);
            response.sendRedirect("RegistrarDashboard");
        } else {
            showDashboard(response);
        }
    }

    private void toggleStatus(String id, String status) {
        String newStatus = "Active".equals(status) ? "Inactive" : "Active";
        try (Connection conn = DBConnection.getRegistrarConn();
             PreparedStatement ps = conn.prepareStatement("UPDATE students SET status = ? WHERE id_card = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, id);
            ps.executeUpdate();
            // Sync with Cafeteria DB
            SyncService.performSync(); 
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void showDashboard(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding: 20px;'>");
        out.println("<h1 style='text-align:center; color:#48cae4;'>Registrar Full Student Records</h1>");
        out.println("<div style='text-align:center; margin-bottom: 20px;'><a href='registrar_admin.html' class='nav-btn'>Add New Student</a></div>");
        
        // Container with overflow-x auto allows horizontal scrolling for large tables
        out.println("<div class='glass-panel' style='width:95%; margin:auto; overflow-x: auto;'>");
        out.println("<table class='modern-table' style='font-size: 0.8rem;'>");
        out.println("<tr><th>Photo</th><th>ID</th><th>Name</th><th>Dept</th><th>Faculty</th><th>Year</th><th>Age</th><th>Gender</th><th>Adm Date</th><th>Birth Date</th><th>Status</th><th>Actions</th></tr>");
        
        try (Connection conn = DBConnection.getRegistrarConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            
            while(rs.next()) {
                String id = rs.getString("id_card");
                String status = rs.getString("status");
                String btnText = status.equals("Active") ? "Deactivate" : "Activate";
                String btnColor = status.equals("Active") ? "#e74c3c" : "#2ecc71";
                
                out.println("<tr>");
                out.println("<td><img src='"+rs.getString("photo_path")+"' width='40' style='border-radius:50%;'></td>");
                out.println("<td>"+id+"</td>");
                out.println("<td>"+rs.getString("first_name")+" "+rs.getString("last_name")+"</td>");
                out.println("<td>"+rs.getString("department")+"</td>");
                out.println("<td>"+rs.getString("faculty")+"</td>");
                out.println("<td>"+rs.getInt("academic_year")+"</td>");
                out.println("<td>"+rs.getInt("age")+"</td>");
                out.println("<td>"+rs.getString("gender")+"</td>");
                out.println("<td>"+rs.getDate("admission_date")+"</td>");
                out.println("<td>"+rs.getDate("birth_date")+"</td>");
                out.println("<td><b>"+status+"</b></td>");
                out.println("<td><a href='RegistrarDashboard?action=toggle&id="+id+"&status="+status+"' style='background:"+btnColor+"; color:white; padding:5px 8px; border-radius:5px; text-decoration:none;'>"+btnText+"</a></td>");
                out.println("</tr>");
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
        out.println("</table></div></body></html>");
    }
}