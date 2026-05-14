package com.campus.service;

import com.campus.db.DBConnection;
import java.sql.*;

/**
 * Enhanced SyncService for Distributed Systems.
 * Implements: Asynchronous Processing, Fault Tolerance (Retries), and Monitoring.
 */
public class SyncService {

    // Public entry point: Runs the sync in a background thread (Asynchronous)
    public static void performSync() {
        new Thread(() -> {
            System.out.println("[Distributed-Sync] Starting background synchronization bridge...");
            executeSyncWithRetries();
        }).start();
    }

    // Internal logic with Fault Tolerance (Retry Pattern)
    private static void executeSyncWithRetries() {
        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            attempt++;
            try {
                syncData();
                success = true;
                System.out.println("[Distributed-Sync] SUCCESS: Data synchronization completed on attempt #" + attempt);
            } catch (SQLException e) {
                System.err.println("[Distributed-Sync] FAILURE: Attempt #" + attempt + " failed. Reason: " + e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        System.out.println("[Distributed-Sync] Retrying in 3 seconds...");
                        Thread.sleep(3000); // Wait before retrying (simple backoff)
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("[Distributed-Sync] CRITICAL: Max retries reached. Sync bridge failed.");
                }
            }
        }
    }

    // Core data migration logic (Modular design)
    private static void syncData() throws SQLException {
        String selectSql = "SELECT * FROM registrar_db.students";
        String upsertSql = "INSERT INTO cafeteria.cached_students (id_card, full_name, department, age, gender, status, photo_path) " +
                           "VALUES (?, CONCAT(?, ' ', ?), ?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), department=VALUES(department), status=VALUES(status)";

        try (Connection regConn = DBConnection.getRegistrarConn();
             Connection cafeConn = DBConnection.getCafeConn();
             Statement stmt = regConn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql);
             PreparedStatement ps = cafeConn.prepareStatement(upsertSql)) {

            int count = 0;
            while (rs.next()) {
                ps.setString(1, rs.getString("id_card"));
                ps.setString(2, rs.getString("first_name"));
                ps.setString(3, rs.getString("last_name"));
                ps.setString(4, rs.getString("department"));
                ps.setInt(5, rs.getInt("age"));
                ps.setString(6, rs.getString("gender"));
                ps.setString(7, rs.getString("status"));
                ps.setString(8, rs.getString("photo_path"));
                ps.executeUpdate();
                count++;
            }
            System.out.println("[Distributed-Sync] Processed " + count + " student records.");
        }
    }
}