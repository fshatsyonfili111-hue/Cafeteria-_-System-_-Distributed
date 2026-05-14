import java.sql.*;
public class TableLister {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables("cafeteria", null, "%", null);
            System.out.println("Tables in 'cafeteria' database:");
            while (rs.next()) {
                System.out.println("- " + rs.getString(3));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
