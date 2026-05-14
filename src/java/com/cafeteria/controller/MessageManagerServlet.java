package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/MessageManager")
public class MessageManagerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if ("delete".equals(action)) {
            deleteMessage(id);
            response.sendRedirect("MessageManager?action=list");
        } else if ("edit".equals(action)) {
            showEditForm(response, id);
        } else {
            showList(response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        // This handles the update logic for existing messages
        String sql = "UPDATE campus_messages SET title=?, message_body=? WHERE id=?";
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getParameter("title"));
            ps.setString(2, request.getParameter("message"));
            ps.setString(3, id);
            ps.executeUpdate();
            response.sendRedirect("MessageManager?action=list");
        } catch (SQLException e) { response.getWriter().write("Update Error: " + e.getMessage()); }
    }

    private void renderNav(PrintWriter out) {
        out.println("<div style='text-align:center; margin-bottom: 30px;'><nav class='navbar-pill'>");
        out.println("<a href='index.html' class='nav-link'><i class='fas fa-home'></i> Home</a>");
        out.println("<a href='DashboardServlet?view=students' class='nav-link'><i class='fas fa-user-graduate'></i> Students</a>");
        out.println("<a href='face_verify.html' class='nav-link'><i class='fas fa-id-badge'></i> AI Face Verify</a>");
        out.println("<a href='scanner.html' class='nav-link'><i class='fas fa-qrcode'></i> QR Scanner</a>");
        out.println("<a href='DashboardServlet?view=meals' class='nav-link'><i class='fas fa-utensils'></i> Meal Activity</a>");
        out.println("<a href='menu_admin.html' class='nav-link'><i class='fas fa-plus-circle'></i> Add Menu</a>");
        out.println("<a href='MenuManager?action=list' class='nav-link'><i class='fas fa-calendar-alt'></i> Manage Menu</a>");
        out.println("<a href='message_admin.html' class='nav-link'><i class='fas fa-bullhorn'></i> Announcements</a>");
        out.println("<a href='MessageManager?action=list' class='nav-link active'><i class='fas fa-history'></i> Msg History</a>");
        out.println("<a href='DashboardServlet?view=feedback' class='nav-link'><i class='fas fa-comments'></i> Feedback</a>");
        out.println("<a href='DashboardServlet?view=reports' class='nav-link'><i class='fas fa-chart-bar'></i> Reports</a>");
        out.println("<a href='index.html' class='nav-link' style='background: rgba(255,0,0,0.15); border-radius: 40px; margin-left: 10px;'><i class='fas fa-sign-out-alt'></i> Logout</a>");
        out.println("</nav></div>");
    }

    private void showList(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Message History</title><link rel='stylesheet' href='css/style.css'><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'></head><body style='padding:20px;'>");
        renderNav(out);
        out.println("<h1 style='text-align:center;'>Announcement History</h1>");
        out.println("<div class='glass-panel' style='width:95%; margin:auto; overflow-x:auto;'>");
        out.println("<table style='width:100%; border-collapse: collapse; color: white;'>");
        out.println("<tr style='background: rgba(255,255,255,0.1);'><th style='padding:15px; text-align:left;'>Title</th><th style='padding:15px; text-align:left;'>Message Content</th><th style='padding:15px; text-align:center;'>Actions</th></tr>");
        
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT * FROM campus_messages ORDER BY created_at DESC")) {
            while(rs.next()) {
                String id = rs.getString("id");
                out.println("<tr style='border-bottom: 1px solid rgba(255,255,255,0.05);'>");
                out.println("<td style='padding:15px; font-weight:600;'>"+rs.getString("title")+"</td>");
                out.println("<td style='padding:15px; opacity:0.8;'>"+rs.getString("message_body")+"</td>");
                out.println("<td style='padding:15px; text-align:center; white-space:nowrap;'>");
                out.println("<a href='MessageManager?action=edit&id="+id+"' class='btn' style='padding:8px 15px; font-size:0.8rem; margin-right:5px;'><i class='fas fa-edit'></i> Edit</a>");
                out.println("<a href='MessageManager?action=delete&id="+id+"' class='btn' style='padding:8px 15px; font-size:0.8rem; background:var(--danger);' onclick='return confirm(\"Are you sure?\")'><i class='fas fa-trash'></i></a>");
                out.println("</td></tr>");
            }
        } catch (SQLException e) { out.println("<tr><td colspan='3'>"+e.getMessage()+"</td></tr>"); }
        out.println("</table></div></body></html>");
    }

    private void showEditForm(HttpServletResponse response, String id) throws IOException {
        PrintWriter out = response.getWriter();
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM campus_messages WHERE id=?")) {
            ps.setString(1, id); ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px; display:flex; justify-content:center; align-items:center; min-height:100vh;'>");
                out.println("<form action='MessageManager' method='POST' class='glass-panel' style='width:450px;'>");
                out.println("<h2 style='text-align:center;'>Edit Announcement</h2><input type='hidden' name='id' value='"+id+"'>");
                out.println("<label>Title:</label><input type='text' name='title' value='"+rs.getString("title")+"'>");
                out.println("<label>Message Body:</label><textarea name='message' style='width:100%; height:120px; margin-bottom:20px;'>"+rs.getString("message_body")+"</textarea>");
                out.println("<button type='submit' class='btn' style='width:100%;'>Update Announcement</button>");
                out.println("<a href='MessageManager?action=list' style='display:block; text-align:center; margin-top:15px; color:white; text-decoration:none; opacity:0.7;'>Cancel</a>");
                out.println("</form></body></html>");
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
    }

    private void deleteMessage(String id) {
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM campus_messages WHERE id=?")) {
            ps.setString(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}