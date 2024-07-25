/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.transacciones;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
/**
 *
 * @author Massielle
 */
public class Transacciones {
    private static int phoneInputCount = 0;
    private static final JPanel phonePanel = new JPanel();

    public static void main(String[] args) {

        // Crear un cliente
        Cliente cliente = new Cliente(0, "Juan", "Pérez");

        // Crear un teléfono asociado al cliente
        Telefono telefono = new Telefono(0, "123-456-7890", 0);

        // Insertar cliente y teléfono en la base de datos
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insertar cliente y obtener el ID generado
            String insertClienteSQL = "INSERT INTO Cliente (nombre, apellido) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertClienteSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, cliente.getNombre());
                pstmt.setString(2, cliente.getApellido());
                pstmt.executeUpdate();

                // Obtener el ID del cliente insertado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int clienteId = generatedKeys.getInt(1);
                        cliente.setId(clienteId); 
                        telefono.setClienteId(clienteId); 
                    } else {
                        throw new SQLException("Failed to obtain client ID.");
                    }
                }
            }

            // Insertar teléfono
            String insertTelefonoSQL = "INSERT INTO Telefono (numero, Cliente_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertTelefonoSQL)) {
                pstmt.setString(1, telefono.getNumero());
                pstmt.setInt(2, telefono.getClienteId());
                pstmt.executeUpdate();
            }

            // Verificar los datos insertados
            System.out.println("Clientes:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente")) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", Nombre: " + rs.getString("nombre") +
                                       ", Apellido: " + rs.getString("apellido"));
                }
            }

            System.out.println("\nTeléfonos:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Telefono")) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", Número: " + rs.getString("numero") +
                                       ", ClienteID: " + rs.getInt("Cliente_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Transacciones");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 2)); 

            JTextField nombreField = new JTextField();
            JTextField apellidoField = new JTextField();
            JTextField direccionField = new JTextField();

            inputPanel.add(new JLabel("Nombre:"));
            inputPanel.add(nombreField);
            inputPanel.add(new JLabel("Apellido:"));
            inputPanel.add(apellidoField);
            inputPanel.add(new JLabel("Dirección:"));
            inputPanel.add(direccionField);

            phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.Y_AXIS));
            JScrollPane phoneScrollPane = new JScrollPane(phonePanel);
            phoneScrollPane.setBorder(BorderFactory.createTitledBorder("Números de Teléfono"));

            JButton addPhoneButton = new JButton("Agregar Número");
            addPhoneButton.addActionListener((ActionEvent e) -> {
                addPhoneInput();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton buttonStart = new JButton("Comenzar Transaccion");
            JButton buttonSave = new JButton("Guardar");
            JButton buttonCommit = new JButton("Commit");
            JButton buttonRollback = new JButton("Rollback");

            buttonPanel.add(buttonStart);
            buttonPanel.add(buttonSave);
            buttonPanel.add(buttonCommit);
            buttonPanel.add(buttonRollback);

            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BorderLayout());
            southPanel.add(addPhoneButton, BorderLayout.NORTH);
            southPanel.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(phoneScrollPane, BorderLayout.CENTER);
            frame.add(southPanel, BorderLayout.SOUTH);

            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }

    private static void addPhoneInput() {
        phoneInputCount++;
        JTextField phoneField = new JTextField(15);
        phonePanel.add(new JLabel("Número de Teléfono " + phoneInputCount + ":"));
        phonePanel.add(phoneField);
        phonePanel.revalidate();
        phonePanel.repaint();
    }
}