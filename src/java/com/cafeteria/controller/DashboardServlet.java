package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String view = request.getParameter("view");
        String id = request.getParameter("id");
        String status = request.getParameter("status");

        if ("toggle".equals(action)) {
            toggleStatus(id, status);
            response.sendRedirect("DashboardServlet?view=students");
        } else if ("edit".equals(action)) {
            showEditForm(response, id);
        } else if ("history".equals(action)) {
            showStudentMealHistory(response, id);
        } else if ("reports".equals(view)) {
            showReportFilters(response);
        } else if ("generateReport".equals(action)) {
            generateReport(request, response);
        } else if ("meals".equals(view)) {
            showMealRecords(response);
        } else if ("feedback".equals(view)) {
            showFeedback(response);
        } else {
            showDashboard(response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "UPDATE cached_students SET full_name=?, department=?, student_type=? WHERE id_card=?";
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getParameter("fullName"));
            ps.setString(2, request.getParameter("department"));
            ps.setString(3, request.getParameter("studentType"));
            ps.setString(4, request.getParameter("id"));
            ps.executeUpdate();
            response.sendRedirect("DashboardServlet?view=students");
        } catch (Exception e) { response.getWriter().write("Update Error: " + e.getMessage()); }
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
        out.println("<a href='MessageManager?action=list' class='nav-link'><i class='fas fa-history'></i> Msg History</a>");
        out.println("<a href='DashboardServlet?view=feedback' class='nav-link'><i class='fas fa-comments'></i> Feedback</a>");
        out.println("<a href='DashboardServlet?view=reports' class='nav-link'><i class='fas fa-chart-bar'></i> Reports</a>");
        out.println("<a href='index.html' class='nav-link' style='background: rgba(255,0,0,0.15); border-radius: 40px; margin-left: 10px;'><i class='fas fa-sign-out-alt'></i> Logout</a>");
        out.println("</nav></div>");
    }

    private void showDashboard(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='css/style.css'><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'></head><body style='padding: 20px;'>");
        renderNav(out);
        
        // STATISTICS SECTION
        out.println("<div style='display:flex; gap:20px; justify-content:center; margin-bottom:30px;'>");
        renderStatCard(out, "Total Students", "SELECT COUNT(*) FROM cached_students", "var(--accent-primary)");
        renderStatCard(out, "Active Today", "SELECT COUNT(*) FROM meal_records WHERE meal_date = CURDATE()", "var(--success)");
        renderStatCard(out, "Feedback", "SELECT COUNT(*) FROM feedback", "var(--warning)");
        out.println("</div>");

        out.println("<h2 style='text-align:center;'>Registered Student Profiles</h2>");
        out.println("<div class='glass-panel' style='width:98%; margin:auto; overflow-x:auto;'><table class='modern-table'><tr><th>Avatar</th><th>ID Card</th><th>Full Name</th><th>Department</th><th>Status</th><th>Category</th><th>Control</th></tr>");
        
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM cached_students")) {
            while(rs.next()) {
                String id = rs.getString("id_card");
                String status = rs.getString("status");
                String color = status.equals("Active") ? "var(--success)" : "var(--danger)";
                out.println("<tr><td><img src='"+rs.getString("photo_path")+"' width='45' height='45' style='border-radius:50%; object-fit:cover; border:2px solid var(--accent-primary);'></td>");
                out.println("<td>"+id+"</td><td>"+rs.getString("full_name")+"</td><td>"+rs.getString("department")+"</td><td style='color:"+color+"; font-weight:bold;'>"+status+"</td><td>"+rs.getString("student_type")+"</td>");
                out.println("<td><a href='DashboardServlet?action=edit&id="+id+"' class='nav-btn' style='padding:6px 12px; font-size:0.8rem;'>Edit</a> ");
                out.println("<a href='biometric_enroll.html?id="+id+"' class='nav-btn' style='padding:6px 12px; font-size:0.8rem; background:var(--accent-primary);'><i class='fas fa-fingerprint'></i> Enroll</a> ");
                out.println("<a href='DashboardServlet?action=resetPassword&id="+id+"' class='nav-btn' style='padding:6px 12px; font-size:0.8rem; background:var(--warning);' onclick='return confirm(\"Reset password to 123456?\")'>Reset</a> ");
                out.println("<a href='DashboardServlet?action=history&id="+id+"' class='nav-btn' style='padding:6px 12px; font-size:0.8rem; background:var(--accent-secondary);'>History</a> ");
                out.println("<a href='DashboardServlet?action=toggle&id="+id+"&status="+status+"' class='nav-btn' style='padding:6px 12px; font-size:0.8rem; background:"+color+";'>Toggle</a></td></tr>");
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
        out.println("</table></div>");
        renderFooter(out);
        out.println("</body></html>");
    }

    private void renderStatCard(PrintWriter out, String label, String sql, String color) {
        int count = 0;
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if(rs.next()) count = rs.getInt(1);
        } catch (SQLException e) {}
        out.println("<div class='glass-panel' style='flex:1; text-align:center; min-width:200px; padding:20px;'>");
        out.println("<h4 style='color:var(--text-secondary); margin-bottom:5px; font-size:0.9rem;'>"+label+"</h4>");
        out.println("<h2 style='-webkit-text-fill-color: "+color+"; margin-bottom:0;'>"+count+"</h2>");
        out.println("</div>");
    }

    private void toggleStatus(String id, String status) {
        String newStatus = "Active".equals(status) ? "Inactive" : "Active";
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("UPDATE cached_students SET status = ? WHERE id_card = ?")) {
            ps.setString(1, newStatus); ps.setString(2, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void showMealRecords(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'>");
        renderNav(out);
        out.println("<h2 style='text-align:center;'>Recent Meal Activity</h2>");
        out.println("<div class='glass-panel' style='width:80%; margin:auto;'><table class='modern-table'><tr><th>Student ID</th><th>Meal Type</th><th>Date Consumed</th></tr>");
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM meal_records ORDER BY meal_date DESC LIMIT 100")) {
            while(rs.next()) { out.println("<tr><td>"+rs.getString("id_card")+"</td><td>"+rs.getString("meal_type")+"</td><td>"+rs.getDate("meal_date")+"</td></tr>"); }
        } catch (SQLException e) { out.println(e.getMessage()); }
        out.println("</table></div>");
        renderFooter(out);
        out.println("</body></html>");
    }

    private void showEditForm(HttpServletResponse response, String id) throws IOException {
        PrintWriter out = response.getWriter();
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ?")) {
            ps.setString(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'><div class='glass-panel' style='width:450px; margin:auto;'><h2>Edit Student: "+id+"</h2><form action='DashboardServlet' method='POST'><input type='hidden' name='id' value='"+id+"'><label>Full Name:</label><input type='text' name='fullName' value='"+rs.getString("full_name")+"'><label>Department:</label><input type='text' name='department' value='"+rs.getString("department")+"'><label>Cafeteria Status:</label><select name='studentType'><option "+(rs.getString("student_type").equals("Cafe")?"selected":"")+">Cafe</option><option "+(rs.getString("student_type").equals("Non-Cafe")?"selected":"")+">Non-Cafe</option></select><br><button type='submit' class='btn' style='width:100%;'>Save Changes</button></form></div></body></html>");
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
    }

    private void showStudentMealHistory(HttpServletResponse response, String id) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'><div style='text-align:center; margin-bottom:20px;'><a href='DashboardServlet?view=students' class='nav-btn'>Back to Records</a></div><h2 style='text-align:center;'>Meal History for "+id+"</h2><div class='glass-panel' style='width:60%; margin:auto;'><table class='modern-table'><tr><th>Meal</th><th>Date</th></tr>");
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("SELECT meal_type, meal_date FROM meal_records WHERE id_card = ? ORDER BY meal_date DESC")) {
            ps.setString(1, id); ResultSet rs = ps.executeQuery();
            while(rs.next()) { out.println("<tr><td>"+rs.getString("meal_type")+"</td><td>"+rs.getDate("meal_date")+"</td></tr>"); }
        } catch (SQLException e) { out.println(e.getMessage()); }
        out.println("</table></div>");
        renderFooter(out);
        out.println("</body></html>");
    }

    private void showReportFilters(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'>");
        renderNav(out);
        out.println("<div class='glass-panel' style='width:450px; margin:auto;'><h2>Report Generator</h2><form action='DashboardServlet' method='GET'><input type='hidden' name='action' value='generateReport'><label>Report Type:</label><select name='reportType'><option value='daily'>Daily Summary</option><option value='monthly'>Monthly Overview</option></select><label>Target Date:</label><input type='date' name='reportDate' required><label>Meal Filter:</label><select name='mealType'><option>Breakfast</option><option>Lunch</option><option>Dinner</option></select><br><button type='submit' class='btn' style='width:100%;'>Generate Analytics</button></form></div></body></html>");
    }

    private void generateReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("reportType"); String date = request.getParameter("reportDate"); String meal = request.getParameter("mealType");
        PrintWriter out = response.getWriter();
        String sql = "daily".equals(type) ? "SELECT * FROM meal_records WHERE meal_date = ? AND meal_type = ?" : "SELECT * FROM meal_records WHERE DATE_FORMAT(meal_date, '%Y-%m') = ? AND meal_type = ?";
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "monthly".equals(type) ? date.substring(0, 7) : date);
            ps.setString(2, meal); ResultSet rs = ps.executeQuery();
            out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'><div style='text-align:center; margin-bottom:20px;'><a href='DashboardServlet?view=reports' class='nav-btn'>New Report</a></div><h2 style='text-align:center;'>Analytics Result: "+type.toUpperCase()+"</h2><div class='glass-panel' style='width:80%; margin:auto;'><table class='modern-table'><tr><th>Student ID</th><th>Meal Type</th><th>Date</th></tr>");
            while(rs.next()) { out.println("<tr><td>"+rs.getString("id_card")+"</td><td>"+rs.getString("meal_type")+"</td><td>"+rs.getDate("meal_date")+"</td></tr>"); }
            out.println("</table></div>");
        renderFooter(out);
        out.println("</body></html>");
        } catch (SQLException e) { out.println(e.getMessage()); }
    }

    private void showFeedback(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body style='padding:20px;'>");
        renderNav(out);
        out.println("<h2 style='text-align:center;'>Student Feedback & Ratings</h2>");
        out.println("<div class='glass-panel' style='width:90%; margin:auto;'><table class='modern-table'><tr><th>Student ID</th><th>Star Rating</th><th>Comment</th></tr>");
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM feedback ORDER BY id DESC")) {
            while(rs.next()) { 
                int stars = rs.getInt("rating");
                String starStr = "⭐".repeat(stars);
                out.println("<tr><td>"+rs.getString("id_card")+"</td><td style='font-size:1.2rem;'>"+starStr+"</td><td>"+rs.getString("comment")+"</td></tr>"); 
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
        out.println("</table></div>");
        renderFooter(out);
        out.println("</body></html>");
    }

    private void resetStudentPassword(String id) {
        try (Connection conn = DBConnection.getRegistrarConn(); 
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = '123' WHERE id = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void renderFooter(PrintWriter out) {
        out.println("<footer class='main-footer' style='margin-top:50px;'><div class='footer-content'>");
        out.println("<div class='footer-section'><h3>AKU ADMIN</h3><p>Management Portal for Aksum University Cafeteria Operations.</p></div>");
        out.println("<div class='footer-section'><h3>Support</h3><ul class='footer-links'><li><a href='mailto:admin@aku.edu.et'>Admin Mail</a></li><li><a href='#'>Tech Support</a></li></ul></div>");
        out.println("</div><div class='footer-bottom'>&copy; 2026 Aksum University Operations</div></footer>");
    }
}