package com.campus.db;

import java.sql.*;

public class DBConnection {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getRegistrarConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/registrar_db", "root", "root");
    }

    public static Connection getCafeConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", "root", "root");
    }
}