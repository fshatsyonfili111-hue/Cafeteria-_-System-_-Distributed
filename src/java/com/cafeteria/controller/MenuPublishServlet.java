package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/MenuPublishServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5)
public class MenuPublishServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Setup path for menu images
        String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // 2. Upload Photo
        Part filePart = request.getPart("photo");
        String fileName = "menu_" + System.currentTimeMillis() + ".jpg";
        filePart.write(uploadPath + File.separator + fileName);

        // 3. Updated SQL to match your new "Weekly" table structure
        String sql = "INSERT INTO menu_schedule (day_of_week, meal_type, start_time, end_time, menu_photo, description) VALUES (?,?,?,?,?,?)";
        
        try (Connection conn = DBConnection.getCafeConn(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String description = request.getParameter("description");
            String ecDay = request.getParameter("ecDay");
            String ecMonth = request.getParameter("ecMonth");
            if (ecDay != null && !ecDay.isEmpty() && ecMonth != null && !ecMonth.isEmpty()) {
                description = "[" + ecMonth + " " + ecDay + "] " + description;
            }

            // Set parameters based on the new form fields
            ps.setString(1, request.getParameter("dayOfWeek")); 
            ps.setString(2, request.getParameter("mealType")); 
            ps.setTime(3, Time.valueOf(request.getParameter("startTime") + ":00"));
            ps.setTime(4, Time.valueOf(request.getParameter("endTime") + ":00"));
            ps.setString(5, "images/" + fileName);
            ps.setString(6, description);
            
            ps.executeUpdate();
            
            response.sendRedirect("MenuManager?action=list");
        } catch (Exception e) { 
            response.getWriter().write("Database Error: " + e.getMessage()); 
        }
    }
}
