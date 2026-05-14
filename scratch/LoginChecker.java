import java.sql.*;

public class LoginChecker {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            System.out.println("--- Checking Student Login Data ---");
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_card, full_name, app_password FROM cached_students LIMIT 5");
            while(rs.next()) {
                String pass = rs.getString("app_password");
                System.out.println("ID: " + rs.getString("id_card") + " | Name: " + rs.getString("full_name") + " | Password: " + (pass == null ? "NULL (NEEDS SETTING)" : pass));
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
