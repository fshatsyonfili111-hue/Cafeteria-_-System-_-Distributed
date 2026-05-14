package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/MenuManager")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5)
public class MenuManagerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if ("delete".equals(action)) {
            deleteMenu(id);
            response.sendRedirect("MenuManager?action=list");
        } else if ("edit".equals(action)) {
            showEditForm(response, id);
        } else {
            showList(response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Part filePart = null;
        try { filePart = request.getPart("photo"); } catch (Exception e) {}
        
        boolean hasNewPhoto = (filePart != null && filePart.getSize() > 0);
        String sql;
        if (hasNewPhoto) {
            sql = "UPDATE menu_schedule SET day_of_week=?, meal_type=?, start_time=?, end_time=?, description=?, menu_photo=? WHERE id=?";
        } else {
            sql = "UPDATE menu_schedule SET day_of_week=?, meal_type=?, start_time=?, end_time=?, description=? WHERE id=?";
        }

        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getParameter("dayOfWeek"));
            ps.setString(2, request.getParameter("mealType"));
            ps.setTime(3, Time.valueOf(request.getParameter("startTime") + (request.getParameter("startTime").length() == 5 ? ":00" : "")));
            ps.setTime(4, Time.valueOf(request.getParameter("endTime") + (request.getParameter("endTime").length() == 5 ? ":00" : "")));
            
            String description = request.getParameter("description");
            String ecDay = request.getParameter("ecDay");
            String ecMonth = request.getParameter("ecMonth");
            if (ecDay != null && !ecDay.isEmpty() && ecMonth != null && !ecMonth.isEmpty()) {
                description = "[" + ecMonth + " " + ecDay + "] " + description;
            }
            ps.setString(5, description);
            
            if (hasNewPhoto) {
                String fileName = "menu_" + System.currentTimeMillis() + ".jpg";
                String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
                filePart.write(uploadPath + File.separator + fileName);
                ps.setString(6, "images/" + fileName);
                ps.setString(7, id);
            } else {
                ps.setString(6, id);
            }
            
            ps.executeUpdate();
            response.sendRedirect("MenuManager?action=list");
        } catch (Exception e) { 
            response.getWriter().write("Update Error: " + e.getMessage()); 
        }
    }

    private void renderNav(PrintWriter out) {
        out.println("<div style='text-align:center; margin-bottom: 30px;'><nav class='navbar-pill'>");
        out.println("<a href='index.html' class='nav-link'><i class='fas fa-home'></i> Home</a>");
        out.println("<a href='DashboardServlet?view=students' class='nav-link'><i class='fas fa-user-graduate'></i> Students</a>");
        out.println("<a href='face_verify.html' class='nav-link'><i class='fas fa-id-badge'></i> AI Face Verify</a>");
        out.println("<a href='scanner.html' class='nav-link'><i class='fas fa-qrcode'></i> QR Scanner</a>");
        out.println("<a href='DashboardServlet?view=meals' class='nav-link'><i class='fas fa-utensils'></i> Meal Activity</a>");
        out.println("<a href='menu_admin.html' class='nav-link'><i class='fas fa-plus-circle'></i> Add Menu</a>");
        out.println("<a href='MenuManager?action=list' class='nav-link active'><i class='fas fa-calendar-alt'></i> Manage Menu</a>");
        out.println("<a href='message_admin.html' class='nav-link'><i class='fas fa-bullhorn'></i> Announcements</a>");
        out.println("<a href='MessageManager?action=list' class='nav-link'><i class='fas fa-history'></i> Msg History</a>");
        out.println("<a href='DashboardServlet?view=feedback' class='nav-link'><i class='fas fa-comments'></i> Feedback</a>");
        out.println("<a href='DashboardServlet?view=reports' class='nav-link'><i class='fas fa-chart-bar'></i> Reports</a>");
        out.println("<a href='index.html' class='nav-link' style='background: rgba(255,0,0,0.15); border-radius: 40px; margin-left: 10px;'><i class='fas fa-sign-out-alt'></i> Logout</a>");
        out.println("</nav></div>");
    }

    private void showList(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Manage Menu</title><link rel='stylesheet' href='css/style.css'><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'></head><body style='padding:20px;'>");
        renderNav(out);
        out.println("<h1 style='text-align:center;'>Scheduled Menus</h1>");
        out.println("<div class='glass-panel' style='width:95%; margin:auto; overflow-x:auto;'>");
        out.println("<table style='width:100%; border-collapse: collapse; color: white;'>");
        out.println("<tr style='background: rgba(255,255,255,0.1);'><th style='padding:15px;'>Photo</th><th style='padding:15px;'>Day</th><th style='padding:15px;'>Meal</th><th style='padding:15px;'>Time</th><th style='padding:15px;'>Description</th><th style='padding:15px;'>Actions</th></tr>");
        
        try (Connection conn = DBConnection.getCafeConn(); Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu_schedule ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), FIELD(meal_type, 'Breakfast', 'Lunch', 'Dinner')")) {
            while(rs.next()) {
                String id = rs.getString("id");
                out.println("<tr style='border-bottom: 1px solid rgba(255,255,255,0.05);'>");
                out.println("<td style='padding:10px; text-align:center;'><img src='"+rs.getString("menu_photo")+"' style='width:60px; height:60px; border-radius:10px; object-fit:cover;'></td>");
                out.println("<td style='padding:15px; text-align:center;'>"+rs.getString("day_of_week")+"</td>");
                out.println("<td style='padding:15px; text-align:center;'>"+rs.getString("meal_type")+"</td>");
                out.println("<td style='padding:15px; text-align:center;'>"+rs.getString("start_time").substring(0,5)+" - "+rs.getString("end_time").substring(0,5)+"</td>");
                out.println("<td style='padding:15px; text-align:center;'>"+rs.getString("description")+"</td>");
                out.println("<td style='padding:15px; text-align:center; white-space:nowrap;'>");
                out.println("<a href='MenuManager?action=edit&id="+id+"' class='btn' style='padding:8px 15px; font-size:0.8rem; margin-right:5px;'><i class='fas fa-edit'></i> Edit</a>");
                out.println("<a href='MenuManager?action=delete&id="+id+"' class='btn' style='padding:8px 15px; font-size:0.8rem; background:var(--danger);' onclick='return confirm(\"Are you sure?\")'><i class='fas fa-trash'></i></a>");
                out.println("</td></tr>");
            }
        } catch (SQLException e) { out.println("<tr><td colspan='6'>"+e.getMessage()+"</td></tr>"); }
        out.println("</table></div></body></html>");
    }

    private void showEditForm(HttpServletResponse response, String id) throws IOException {
        PrintWriter out = response.getWriter();
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM menu_schedule WHERE id=?")) {
            ps.setString(1, id); ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                out.println("<html><head><link rel='stylesheet' href='css/style.css'><link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'></head><body style='padding:20px; display:flex; justify-content:center; align-items:center; min-height:100vh;'>");
                out.println("<form action='MenuManager' method='POST' enctype='multipart/form-data' class='glass-panel' style='width:450px;'>");
                out.println("<h2 style='text-align:center;'>Edit Menu Item</h2><input type='hidden' name='id' value='"+id+"'>");
                
                out.println("<label>Day:</label><select name='dayOfWeek'>");
                String currentDay = rs.getString("day_of_week");
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                for(String d : days) out.println("<option value='"+d+"' "+(d.equals(currentDay)?"selected":"")+">"+d+"</option>");
                out.println("</select>");

                out.println("<label>Meal:</label><select name='mealType'>");
                String currentMeal = rs.getString("meal_type");
                String[] meals = {"Breakfast", "Lunch", "Dinner"};
                for(String m : meals) out.println("<option value='"+m+"' "+(m.equals(currentMeal)?"selected":"")+">"+m+"</option>");
                out.println("</select>");

                out.println("<label>Start Time:</label><input type='time' name='startTime' value='"+rs.getString("start_time").substring(0,5)+"'>");
                out.println("<label>End Time:</label><input type='time' name='endTime' value='"+rs.getString("end_time").substring(0,5)+"'>");
                
                out.println("<label>Ethiopian Date (Helper):</label><div style='display:flex; gap:10px; margin-bottom:20px;'>");
                out.println("<input type='number' name='ecDay' placeholder='Day' style='width:30%;' min='1' max='30'>");
                out.println("<select name='ecMonth' style='width:70%;'><option value=''>Month (EC)</option><option>Meskerem</option><option>Tikimt</option><option>Hidar</option><option>Tahsas</option><option>Tir</option><option>Yakatit</option><option>Magabit</option><option>Miyazya</option><option>Ginbot</option><option>Sane</option><option>Hamle</option><option>Nehasse</option><option>Pagume</option></select></div>");

                out.println("<label>Description:</label><input type='text' name='description' value='"+rs.getString("description")+"'>");
                out.println("<label>Change Photo (Optional):</label><input type='file' name='photo' accept='image/*'>");
                
                out.println("<button type='submit' class='btn' style='width:100%;'>Update Schedule</button>");
                out.println("<a href='MenuManager?action=list' style='display:block; text-align:center; margin-top:15px; color:white; text-decoration:none; opacity:0.7;'>Cancel</a>");
                out.println("</form></body></html>");
            }
        } catch (SQLException e) { out.println(e.getMessage()); }
    }

    private void deleteMenu(String id) {
        try (Connection conn = DBConnection.getCafeConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM menu_schedule WHERE id=?")) {
            ps.setString(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}