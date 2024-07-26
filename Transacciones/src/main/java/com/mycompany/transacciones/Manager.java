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
        try (Connection conn = DatabaseConnection.getConnection()) {
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
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery("SELECT * FROM Telefono");
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    

    public static boolean insertarCliente(String nombre, String apellido, String direccion){
        try (Connection conn = DatabaseConnection.getConnection()) {
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
        try (Connection conn = DatabaseConnection.getConnection()) {
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
}
