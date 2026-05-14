import java.sql.*;

public class CheckSchema {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "cached_students", null);
            System.out.println("--- Columns in cached_students ---");
            while(rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + ")");
            }
            
            System.out.println("\n--- Checking for Student 9 ---");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM cached_students WHERE id_card = ?");
            ps.setString(1, "9");
            ResultSet srs = ps.executeQuery();
            if (srs.next()) {
                System.out.println("Found Student 9!");
                System.out.println("Full Name: " + srs.getString("full_name"));
                System.out.println("Current Password: " + srs.getString("app_password"));
            } else {
                System.out.println("Student 9 NOT FOUND in cached_students!");
            }
            
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
