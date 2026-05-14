package com.cafeteria.controller;

import com.campus.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.*;

@WebServlet("/DigitalIDServlet")
public class DigitalIDServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String dept = rs.getString("department");
                String photo = rs.getString("photo_path");
                
                String qrData = "ID:" + id + ", Name:" + fullName + ", Dept:" + dept;
                String qrEncoded = URLEncoder.encode(qrData, "UTF-8");
                String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + qrEncoded;

                // --- START HEADER AND CSS ---
                out.println("<html><head>");
                out.println("<link href='https://fonts.googleapis.com/css2?family=Oswald:wght@500&family=Roboto:wght@400;700&display=swap' rel='stylesheet'>");
                out.println("<style>");
                out.println("body { background: #000814; display: flex; flex-direction: column; align-items: center; padding-top: 50px; font-family: 'Roboto', sans-serif; }");
                
                // Updated .id-card with a non-white background
                out.println(".id-card { width: 540px; height: 340px; background: linear-gradient(135deg, #e0e7ff 0%, #f8fafc 100%); border-radius: 12px; position: relative; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.5); }");
                
                // Vertical Banner Styles
                out.println(".vertical-banner { position: absolute; right: 0; top: 0; bottom: 0; width: 60px; background: rgba(255,255,255,0.8); display: flex; align-items: center; justify-content: center; border-left: 2px solid #fca311; }");
                out.println(".vertical-text { transform: rotate(-90deg); white-space: nowrap; font-family: 'Oswald', sans-serif; font-size: 24px; letter-spacing: 2px; }");
                
                // Photo and Details
                out.println(".photo-container { position: absolute; bottom: 30px; left: 30px; z-index: 10; }");
                out.println(".photo-img { width: 130px; height: 130px; border-radius: 50%; border: 4px solid #e63946; object-fit: cover; background: #fff; }");
                out.println(".student-details { position: absolute; top: 100px; left: 120px; text-align: left; }");
                out.println(".name-eng { font-size: 22px; font-weight: bold; color: #1e3a8a; margin: 0; text-shadow: 1px 1px 0px rgba(255,255,255,0.5); }");
                
                // Footer Shapes
                out.println(".footer-graphics { position: absolute; bottom: 0; right: 60px; width: 250px; height: 120px; }");
                out.println(".block { position: absolute; width: 100%; height: 35px; color: white; display: flex; align-items: center; padding-left: 15px; font-size: 14px; font-weight: bold; }");
                out.println(".blue-block { bottom: 70px; background: #0077b6; clip-path: polygon(20% 0%, 100% 0%, 100% 100%, 0% 100%); }");
                out.println(".red-block { bottom: 35px; background: #e63946; clip-path: polygon(10% 0%, 100% 0%, 100% 100%, 0% 100%); }");
                out.println(".orange-block { bottom: 0; background: #fca311; clip-path: polygon(5% 0%, 100% 0%, 100% 100%, 0% 100%); }");
                
                out.println("@media print { .no-print { display: none; } body { background: white; } .id-card { box-shadow: none; border: 1px solid #ccc; background: #f8fafc !important; -webkit-print-color-adjust: exact; } }");
                out.println("</style></head><body>");

                // --- START CARD HTML ---
                out.println("<div class='id-card'>");
                
                out.println("<div class='vertical-banner'><div class='vertical-text'><span style='color:#0077b6;'>STUDENT</span> <span style='color:#e63946;'>ID CARD</span></div></div>");
                out.println("<div style='position:absolute; top:15px; right:80px; background: white; padding: 5px; border-radius: 5px;'><img src='"+qrUrl+"' width='75'></div>");
                out.println("<div class='photo-container'><img src='"+photo+"' class='photo-img'></div>");
                
                out.println("<div class='student-details'>");
                out.println("<p class='name-eng'>"+fullName+"</p>");
                out.println("<p style='font-size:14px; color:#475569; font-weight:bold; margin-top:5px;'>"+dept+"</p>");
                out.println("</div>");
                
                out.println("<div class='footer-graphics'>");
                out.println("<div class='block blue-block'>ID: "+id+"</div>");
                out.println("<div class='block red-block'>0347753645</div>");
                out.println("<div class='block orange-block'>www.aku.edu.et</div>");
                out.println("</div>");
                
                out.println("</div>"); 
                
                out.println("<div style='margin-top:20px;' class='no-print'><button onclick='window.print()' style='padding:10px 30px; background:#0077b6; color:white; border:none; border-radius:5px; cursor:pointer; font-weight:bold;'>Print ID Card</button></div>");
                out.println("</body></html>");
            } else {
                out.println("<html><body><h2>Student not found.</h2></body></html>");
            }
        } catch (SQLException e) { out.println("Error: " + e.getMessage()); }
    }
}