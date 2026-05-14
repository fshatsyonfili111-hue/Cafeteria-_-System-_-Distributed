package com.cafeteria.controller;

import com.campus.db.DBConnection;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {

    // UNIFIED NAV — matches DashboardServlet exactly
    private void renderNav(PrintWriter out, boolean isListPage) {
        out.println("<div style='text-align:center; margin-bottom: 30px;'><nav class='navbar-pill'>");
        out.println("<a href='index.html' class='nav-link'><i class='fas fa-home'></i> Home</a>");
        out.println("<a href='DashboardServlet?view=students' class='nav-link'><i class='fas fa-user-graduate'></i> Students</a>");
        out.println("<a href='face_verify.html' class='nav-link'><i class='fas fa-id-badge'></i> AI Face Verify</a>");
        out.println("<a href='scanner.html' class='nav-link'><i class='fas fa-qrcode'></i> QR Scanner</a>");
        out.println("<a href='DashboardServlet?view=meals' class='nav-link'><i class='fas fa-utensils'></i> Meal Activity</a>");
        out.println("<a href='menu_admin.html' class='nav-link'><i class='fas fa-plus-circle'></i> Add Menu</a>");
        out.println("<a href='MenuManager?action=list' class='nav-link'><i class='fas fa-calendar-alt'></i> Manage Menu</a>");
        out.println("<a href='message_admin.html' class='nav-link'><i class='fas fa-bullhorn'></i> Announcements</a>");
        out.println("<a href='MessageServlet?action=list' class='nav-link" + (isListPage ? " active" : "") + "'><i class='fas fa-history'></i> Msg History</a>");
        out.println("<a href='DashboardServlet?view=feedback' class='nav-link'><i class='fas fa-comments'></i> Feedback</a>");
        out.println("<a href='DashboardServlet?view=reports' class='nav-link'><i class='fas fa-chart-bar'></i> Reports</a>");
        out.println("<a href='index.html' class='nav-link' style='background: rgba(255,0,0,0.15); border-radius: 40px; margin-left: 10px;'><i class='fas fa-sign-out-alt'></i> Logout</a>");
        out.println("</nav></div>");
    }

    // Handles the request to either show the list or serve JSON to mobile
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String format = request.getParameter("format"); // ?format=json for mobile app

        if ("json".equals(format)) {
            sendJsonMessages(response);
        } else if ("list".equals(action)) {
            showList(response);
        } else {
            response.sendRedirect("message_admin.html");
        }
    }

    // PUBLISH MESSAGE
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String body = request.getParameter("message");

        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO campus_messages (title, message_body) VALUES (?,?)")) {
            ps.setString(1, title);
            ps.setString(2, body);
            ps.executeUpdate();
            response.sendRedirect("MessageServlet?action=list");
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    // SHOW HISTORY LIST FOR MANAGER (Web UI)
    private void showList(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head>");
        out.println("<title>Message History | AKU Admin</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'>");
        out.println("</head><body style='padding:20px;'>");

        renderNav(out, true);

        out.println("<h1 style='text-align:center; margin-bottom:30px;'>");
        out.println("<i class='fas fa-history' style='color:var(--accent-primary);'></i> Campus Announcement History</h1>");

        out.println("<div class='glass-panel' style='width:95%; margin:auto; overflow-x:auto;'>");
        out.println("<table class='modern-table'><thead><tr>");
        out.println("<th><i class='fas fa-heading'></i> Title</th>");
        out.println("<th><i class='fas fa-align-left'></i> Message Content</th>");
        out.println("<th><i class='fas fa-clock'></i> Date Sent</th>");
        out.println("</tr></thead><tbody>");

        try (Connection conn = DBConnection.getCafeConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM campus_messages ORDER BY created_at DESC")) {
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td style='font-weight:600; color:var(--accent-primary);'>" + rs.getString("title") + "</td>");
                out.println("<td style='opacity:0.85;'>" + rs.getString("message_body") + "</td>");
                out.println("<td style='color:var(--text-secondary); font-size:0.85rem;'>" + rs.getTimestamp("created_at") + "</td>");
                out.println("</tr>");
            }
        } catch (SQLException e) {
            out.println("<tr><td colspan='3' style='color:var(--danger); text-align:center;'>" + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody></table></div>");

        // Footer
        out.println("<footer class='main-footer' style='margin-top:50px;'><div class='footer-content'>");
        out.println("<div class='footer-section'><h3>AKU ADMIN</h3><p>Management Portal for Aksum University Cafeteria Operations.</p></div>");
        out.println("</div><div class='footer-bottom'>&copy; 2026 Aksum University Operations</div></footer>");
        out.println("</body></html>");
    }

    // SEND JSON TO MOBILE APP
    private void sendJsonMessages(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        List<Map<String, String>> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getCafeConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM campus_messages ORDER BY created_at DESC")) {
            while (rs.next()) {
                Map<String, String> m = new HashMap<>();
                m.put("title", rs.getString("title"));
                m.put("body", rs.getString("message_body"));
                m.put("date", rs.getTimestamp("created_at").toString());
                messages.add(m);
            }
            response.getWriter().write(new Gson().toJson(messages));
        } catch (SQLException e) {
            response.getWriter().write("[]");
        }
    }
}