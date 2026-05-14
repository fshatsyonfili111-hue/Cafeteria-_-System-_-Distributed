import java.sql.*;

public class TestAndFixLogin {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cafeteria";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("--- Testing Login Data for Student ID 9 ---");
            
            // 1. Check if student 9 exists
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ?");
            ps.setString(1, "9");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                System.out.println("FOUND: Student 9 (" + rs.getString("full_name") + ")");
                String currentPass = rs.getString("app_password");
                System.out.println("Current Password: " + (currentPass == null ? "NULL" : currentPass));
                
                // 2. Set password to 1212 as requested
                System.out.println("Setting password to 1212...");
                PreparedStatement ups = conn.prepareStatement("UPDATE cached_students SET app_password = ? WHERE id_card = ?");
                ups.setString(1, "1212");
                ups.setString(2, "9");
                int updated = ups.executeUpdate();
                
                if (updated > 0) {
                    System.out.println("SUCCESS: Password updated to 1212.");
                } else {
                    System.out.println("FAILED: Could not update password.");
                }
                
                // 3. Verify Login
                System.out.println("Verifying login query...");
                PreparedStatement vps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ? AND app_password = ?");
                vps.setString(1, "9");
                vps.setString(2, "1212");
                ResultSet vrs = vps.executeQuery();
                if (vrs.next()) {
                    System.out.println("VERIFIED: Login successful with ID 9 and Password 1212.");
                } else {
                    System.out.println("ERROR: Login still fails after update!");
                }
            } else {
                System.out.println("CRITICAL: Student 9 does NOT exist in cached_students. Sync might be needed.");
                // Try to check registrar_db
                System.out.println("Checking registrar_db for student 9...");
                try (Connection regConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registrar_db", "root", "root")) {
                    PreparedStatement rps = regConn.prepareStatement("SELECT * FROM students WHERE id_card = ?");
                    rps.setString(1, "9");
                    ResultSet rrs = rps.executeQuery();
                    if (rrs.next()) {
                        System.out.println("Found student 9 in registrar_db. Syncing now...");
                        // Manually sync for testing
                        PreparedStatement ips = conn.prepareStatement("INSERT INTO cached_students (id_card, full_name, app_password) VALUES (?, ?, ?)");
                        ips.setString(1, "9");
                        ips.setString(2, rrs.getString("first_name") + " " + rrs.getString("last_name"));
                        ips.setString(3, "1212");
                        ips.executeUpdate();
                        System.out.println("SUCCESS: Student 9 synced and password set to 1212.");
                    } else {
                        System.out.println("ERROR: Student 9 not found in registrar_db either!");
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
