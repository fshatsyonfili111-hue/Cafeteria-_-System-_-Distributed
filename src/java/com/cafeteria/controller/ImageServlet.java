package com.cafeteria.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.Files;

@WebServlet("/DisplayImage")
public class ImageServlet extends HttpServlet {
    // Fixed folder on your computer to share between projects
    private static final String IMAGE_DIR = "C:/CafeteriaSystem/images/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("name");
        if (fileName == null || fileName.isEmpty()) return;

        // Security: Remove "images/" prefix if it exists in the DB string
        if (fileName.startsWith("images/")) {
            fileName = fileName.substring(7);
        }

        File file = new File(IMAGE_DIR, fileName);
        if (!file.exists()) {
            // Fallback to a default image if file is missing
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());
        response.setContentType(contentType != null ? contentType : "image/jpeg");
        response.setContentLength((int) file.length());

        try (FileInputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
