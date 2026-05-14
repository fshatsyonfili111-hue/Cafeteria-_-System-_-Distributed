import java.sql.*;

public class DataChecker {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            
            System.out.println("--- Menu Schedule Photos ---");
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT id, menu_photo FROM menu_schedule LIMIT 5");
            while(rs1.next()) System.out.println("ID: " + rs1.getString("id") + " | Path: " + rs1.getString("menu_photo"));

            System.out.println("\n--- Student Photos ---");
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT id_card, photo_path FROM cached_students LIMIT 5");
            while(rs2.next()) System.out.println("ID: " + rs2.getString("id_card") + " | Path: " + rs2.getString("photo_path"));
            
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
