package com.mycompany.transacciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String DB = "transacciones";
    private static final String URL = "jdbc:mariadb://localhost:3306/"+DB+"?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Dani aqui tus credenciales de MariaDB <3
    private static final String DESK_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "transacciones_backup.sql";
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
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "mariadb-dump -u "+ USER + " -p" + PASSWORD + " --add-drop-database" + " -B " + database + " -r " + DESK_PATH);
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
    
    public static void restore(){
        String host = "localhost";
        String database = "transacciones";
        String file_path = "C:\\Users\\DANIEL\\Documents\\GitHub\\BD2\\transbackuptest.sql";
        String command = "mariadb -u root -p"+PASSWORD + " < " + DESK_PATH;

        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", command);
            Process restore = builder.start();

            int exitCode = restore.waitFor();
            if (exitCode == 0) {
                System.out.println("Restauración completada con éxito.");
            } else {
                System.err.println("Error en la exportación. Código de salida: " + exitCode);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally   {
            try {
                if (!conn.isClosed()) {
                    conn.setCatalog(DB);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        
    }
}
