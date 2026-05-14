import java.sql.*;
public class FaceColumnLister {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns("cafeteria", null, "face_templates", "%");
            System.out.println("Columns in 'face_templates':");
            while (rs.next()) {
                System.out.println("- " + rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + ")");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
