package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String pass = request.getParameter("password");
        String role = request.getParameter("role");

        try (Connection conn = DBConnection.getCafeConn()) {
            
            // 1. STUDENT LOGIN
            if ("student".equals(role)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card=? AND app_password=?");
                ps.setString(1, id); 
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("studentId", id);
                    
                    // FIXED: Only one redirect allowed. Check password first.
                    if ("123456".equals(pass)) {
                        response.sendRedirect("change_password.html?id=" + id);
                    } else {
                        response.sendRedirect("student_menu_view.html?id=" + id);
                    }
                } else { 
                    response.getWriter().write("Login Failed: Incorrect Student ID or Password"); 
                }
            } 
            
            // 2. REGISTRAR LOGIN
            else if ("registrar".equals(role)) {
                if("admin".equals(id) && "123".equals(pass)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("role", "Registrar");
                    response.sendRedirect("RegistrarDashboard");
                } else { 
                    response.getWriter().write("Invalid Registrar Login"); 
                }
            }
            
            // 3. CAFETERIA STAFF LOGIN
            else if ("cafe".equals(role)) {
                PreparedStatement ps = conn.prepareStatement("SELECT role FROM staff_accounts WHERE username=? AND password=?");
                ps.setString(1, id); 
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    String userRole = rs.getString("role");
                    HttpSession session = request.getSession();
                    session.setAttribute("role", userRole);
                    
                    if ("Manager".equals(userRole)) {
                        response.sendRedirect("DashboardServlet");
                    } else {
                        response.sendRedirect("scanner.html");
                    }
                } else {
                    response.getWriter().write("Invalid Staff Login");
                }
            }
        } catch (SQLException e) { 
            response.getWriter().write("Database Error: " + e.getMessage()); 
        }
    }
}