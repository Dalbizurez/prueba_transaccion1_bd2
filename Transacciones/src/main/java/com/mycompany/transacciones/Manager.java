/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DANIEL
 */
public class Manager {

    public static void main() {
        
    }

    public static ResultSet verClientes(){
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery("SELECT * FROM Cliente");
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static ResultSet verTelefonos(){
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery("SELECT * FROM Telefono");
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    
// Función para insertar clientes en la base de datos en base a parametros
// Podría realizar uno para insertar según objeto
    public static boolean insertarCliente(String nombre, String apellido, String direccion){
        try {
            Connection conn = DatabaseConnection.getConnection();
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

    public static boolean insertarTelefono(String numero, int clienteId){
        try {
            Connection conn = DatabaseConnection.getConnection();
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
            Connection conn = DatabaseConnection.getConnection();
            
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
    
    public static boolean actualizarTelefono(int idTelefono, String numero, int clienteId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
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
            Connection conn = DatabaseConnection.getConnection();
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
            Connection conn = DatabaseConnection.getConnection();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean rollback(){
        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void start(int level){
        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
