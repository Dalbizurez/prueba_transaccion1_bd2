package com.mycompany.transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerPostgres {

    public static void main() {
        //
    }

    public static ResultSet verClientes() {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery("SELECT * FROM Cliente");
            return result;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static ResultSet verTelefonos() {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery("SELECT * FROM Telefono");
            return result;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static boolean insertarCliente(String nombre, String apellido, String direccion) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String insertClienteSQL = "INSERT INTO Cliente (nombre, apellido, direccion) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertClienteSQL);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, direccion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertarTelefono(String numero, int clienteId) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String insertTelefonoSQL = "INSERT INTO Telefono (numero, Cliente_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertTelefonoSQL);
            pstmt.setString(1, numero);
            pstmt.setInt(2, clienteId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarCliente(int id, String nombre, String apellido, String direccion) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String updateClienteSQL = "UPDATE Cliente SET nombre = ?, apellido = ?, direccion = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateClienteSQL);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, direccion);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarCliente(int id) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String deleteClienteSQL = "DELETE FROM Cliente WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteClienteSQL);
            pstmt.setInt(1, id);
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarTelefono(int id) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String deleteTelSQL = "DELETE FROM Telefono WHERE Cliente_Id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteTelSQL);
            pstmt.setInt(1, id);
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarTelefono(int idTelefono, String numero, int clienteId) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String updateTelefonoSQL = "UPDATE Telefono SET numero = ?, cliente_id = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateTelefonoSQL);
            pstmt.setString(1, numero);
            pstmt.setInt(2, clienteId);
            pstmt.setInt(3, idTelefono);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean existeCliente(int clienteId) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            String queryClienteSQL = "SELECT COUNT(*) FROM Cliente WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(queryClienteSQL);
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean commit() {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean rollback() {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void start(int level) {
        try {
            Connection conn = DatabaseConnectionPostgres.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(level);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
