package com.mycompany.transacciones;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    
    public static void backup(){
        String host = "localhost";
        String database = "transacciones";
        String file_path = "C:\\Users\\DANIEL\\Documents\\GitHub\\BD2\\transbackuptest.sql";
        String command = "mariadb-dump -u %s -p %s --add-drop-database -B %s > %s";
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "mariadb-dump -u "+ USER + " -p" + PASSWORD + " --add-drop-database" + " -B " + database + " -r " + file_path);
            builder.redirectErrorStream(true);
        
            Process backup = builder.start();
            System.out.println(builder.command());

            
            BufferedReader reader = new BufferedReader(new InputStreamReader(backup.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            int exitCode = backup.waitFor();
            if (exitCode == 0) {
                System.out.println("Exportación completada con éxito.");
            } else {
                System.err.println("Error en la exportación. Código de salida: " + exitCode);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
    
}
