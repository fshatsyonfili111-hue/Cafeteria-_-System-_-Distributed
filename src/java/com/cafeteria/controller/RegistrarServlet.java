package com.cafeteria.controller;

import com.campus.service.SyncService;
import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

@WebServlet("/RegistrarServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5)
public class RegistrarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get raw ID and clean it just in case the scanner sends extra characters
        String rawId = request.getParameter("id");
        String id = rawId.contains(":") ? rawId.split(":")[1].trim() : rawId.trim();
        
        // 2. Handle Photo Upload
        String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
        new File(uploadPath).mkdir();
        Part filePart = request.getPart("photo");
        String fileName = id + ".jpg";
        filePart.write(uploadPath + File.separator + fileName);

        // 3. Insert into Registrar DB
        String sql = "INSERT INTO students (id_card, first_name, last_name, age, gender, department, faculty, academic_year, status, admission_date, birth_date, photo_path) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = DBConnection.getRegistrarConn(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id);
            ps.setString(2, request.getParameter("firstName"));
            ps.setString(3, request.getParameter("lastName"));
            ps.setInt(4, Integer.parseInt(request.getParameter("age")));
            ps.setString(5, request.getParameter("gender"));
            ps.setString(6, request.getParameter("department"));
            ps.setString(7, request.getParameter("faculty"));
            ps.setInt(8, Integer.parseInt(request.getParameter("year")));
            ps.setString(9, "Active");
            ps.setDate(10, Date.valueOf(request.getParameter("admissionDate")));
            ps.setDate(11, Date.valueOf(request.getParameter("birthDate")));
            ps.setString(12, "images/" + fileName);
            
             ps.executeUpdate();
            
            // Trigger Sync Bridge
            SyncService.performSync();
            
            // REMOVE THIS: response.sendRedirect("registrar_admin.html?status=success");
            
            // ADD THIS INSTEAD:
            response.setContentType("text/plain");
            response.getWriter().write("SUCCESS");
            
        } catch (Exception e) { 
            response.getWriter().write("Error: " + e.getMessage()); 
        }
    }
}