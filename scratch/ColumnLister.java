import java.sql.*;
public class ColumnLister {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns("cafeteria", null, "cached_students", "%");
            System.out.println("Columns in 'cached_students':");
            while (rs.next()) {
                System.out.println("- " + rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + ")");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
