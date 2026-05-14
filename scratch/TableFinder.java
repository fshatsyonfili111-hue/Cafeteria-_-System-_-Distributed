import java.sql.*;

public class TableFinder {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registrar_db", "root", "root");
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("--- Tables in registrar_db ---");
            while (rs.next()) {
                System.out.println(rs.getString(3));
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
