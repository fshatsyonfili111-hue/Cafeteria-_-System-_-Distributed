package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/StudentProfileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5)
public class StudentProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getCafeConn()) {
            if ("updatePhoto".equals(action)) {
                updatePhoto(request, id, conn, response);
            } else if ("updatePassword".equals(action)) {
                updatePassword(request, id, conn, response);
            }
        } catch (Exception e) {
            out.write("Profile Error: " + e.getMessage());
        }
    }

    private void updatePhoto(HttpServletRequest request, String id, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Part filePart = request.getPart("photo");
        if (filePart == null || filePart.getSize() == 0) {
            response.getWriter().write("No file selected.");
            return;
        }

        String fileName = "student_" + id + "_" + System.currentTimeMillis() + ".jpg";
        String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        filePart.write(uploadPath + File.separator + fileName);
        String relativePath = "images/" + fileName;

        String sql = "UPDATE cached_students SET photo_path = ? WHERE id_card = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, relativePath);
            ps.setString(2, id);
            ps.executeUpdate();
            response.sendRedirect("student_menu_view.html?id=" + id + "&status=photo_updated");
        }
    }

    private void updatePassword(HttpServletRequest request, String id, Connection conn, HttpServletResponse response) throws IOException, SQLException {
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        if (!newPass.equals(confirmPass)) {
            response.getWriter().write("Passwords do not match.");
            return;
        }

        // 1. Verify old password from 'cached_students' table in Cafeteria DB
        String verifySql = "SELECT * FROM cached_students WHERE id_card = ? AND app_password = ?";
        try (PreparedStatement psVerify = conn.prepareStatement(verifySql)) {
            psVerify.setString(1, id);
            psVerify.setString(2, oldPass);
            ResultSet rs = psVerify.executeQuery();

            if (rs.next()) {
                // 2. Update to new password in Cafeteria DB
                String updateSql = "UPDATE cached_students SET app_password = ? WHERE id_card = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setString(1, newPass);
                    psUpdate.setString(2, id);
                    psUpdate.executeUpdate();
                    response.sendRedirect("student_menu_view.html?id=" + id + "&status=password_updated");
                }
            } else {
                response.getWriter().write("Current password incorrect.");
            }
        }
    }
}
