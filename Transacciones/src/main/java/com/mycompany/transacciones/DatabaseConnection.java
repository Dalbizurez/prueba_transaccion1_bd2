package com.mycompany.transacciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/transacciones?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Dani aqui tus credenciales de MariaDB <3
    private static Connection conn;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()){
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null){
                conn.close();
                System.out.println("Conexion cerrada");
            }
        } catch (Exception e) {
        }

    }
}
