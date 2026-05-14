import java.sql.*;
public class FaceTableDetail {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            DatabaseMetaData md = conn.getMetaData();
            
            System.out.println("--- Columns ---");
            ResultSet rs = md.getColumns("cafeteria", null, "face_templates", "%");
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME") + " | " + rs.getString("TYPE_NAME"));
            }
            
            System.out.println("--- Primary Keys ---");
            ResultSet pks = md.getPrimaryKeys("cafeteria", null, "face_templates");
            while (pks.next()) {
                System.out.println(pks.getString("COLUMN_NAME"));
            }
            
            System.out.println("--- Unique Indexes ---");
            ResultSet idx = md.getIndexInfo("cafeteria", null, "face_templates", true, false);
            while (idx.next()) {
                System.out.println(idx.getString("COLUMN_NAME") + " (Index: " + idx.getString("INDEX_NAME") + ")");
            }
            
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
