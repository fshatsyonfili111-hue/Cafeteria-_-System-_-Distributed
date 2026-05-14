package com.cafeteria.db;

import com.campus.db.DBConnection;
import com.campus.model.Student;
import java.sql.*;
import java.time.LocalDate;

public class MealManager {

    public static Student getStudent(String studentId) throws SQLException {
        try (Connection conn = DBConnection.getCafeConn()) {
            String sql = "SELECT * FROM cached_students WHERE id_card = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String[] names = fullName.split(" ");
                String first = names[0];
                String last = (names.length > 1) ? names[1] : "";
                
                return new Student(
                    rs.getString("id_card"),
                    first,
                    last,
                    0, // Age (not in cached_students)
                    "N/A", // Gender
                    rs.getString("department"),
                    "N/A", // Faculty
                    0,     // Year
                    rs.getString("status"),
                    "N/A", // AdmissionDate
                    "N/A", // BirthDate
                    rs.getString("photo_path"),
                    rs.getString("student_type")
                );
            }
        }
        return null; 
    }

    public static boolean hasAlreadyEaten(String studentId, String mealType) throws SQLException {
        String sql = "SELECT id FROM meal_records WHERE id_card = ? AND meal_type = ? AND meal_date = ?";
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, mealType);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    public static void logMeal(String studentId, String mealType) throws SQLException {
        String query = "INSERT INTO meal_records (id_card, meal_type, meal_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getCafeConn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, studentId);
            ps.setString(2, mealType);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        }
    }
}