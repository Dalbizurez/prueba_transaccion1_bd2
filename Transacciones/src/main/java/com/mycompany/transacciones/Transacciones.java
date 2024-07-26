/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.transacciones;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Massielle
 */
public class Transacciones {
    private static final UI ui = new UI();

    public static void main(String[] args) {
        
        setUpUi();
        
        
    }

    private static void setUpUi(){
        ui.setVisible(true);
        verClientes();
        verTelefonos();
        ui.btnCommit.setEnabled(false);
        ui.btnRollback.setEnabled(false);
        ui.btnGuardar.setEnabled(false);
        ui.btnTelefono.setEnabled(false);
        ui.btnIniciar.addActionListener(e -> {
            ui.btnGuardar.setEnabled(true);
            ui.btnTelefono.setEnabled(true);
            ui.btnCommit.setEnabled(true);
            ui.btnRollback.setEnabled(true);
        });
        ui.btnGuardar.addActionListener(e -> {
            String nombre = ui.txtNombre.getText();
            String apellido = ui.txtApellido.getText();
            String direccion = ui.txtDireccion.getText();
            insertarCliente(nombre, apellido, direccion);
        });
        ui.btnTelefono.addActionListener(e -> {
            String numero = ui.txtTelefono.getText();
            int clienteId = Integer.parseInt(ui.txtIdCliente.getText());
            insertarTelefono(numero, clienteId);
        });
        ui.btnIniciar.addActionListener(e -> {
            ui.btnCommit.setEnabled(true);
            ui.btnRollback.setEnabled(true);
        });
    }

    private static void insertarCliente(String nombre, String apellido, String direccion) {
        if (Manager.insertarCliente(nombre, apellido, direccion)){
            ui.txtApellido.setText("");
            ui.txtNombre.setText("");
            ui.txtDireccion.setText("");
            ui.txtNombre.setToolTipText("");
            verClientes();
            ui.tblClientes.changeSelection(ui.tblClientes.getRowCount() - 1, 0, false, false);
        } else {
            ui.txtNombre.setToolTipText("Error al insertar el cliente");
        }
    }

    private static void insertarTelefono(String numero, int clienteId) {
        if (Manager.insertarTelefono(numero, clienteId)){
            ui.txtTelefono.setText("");
            ui.txtTelefono.setToolTipText("");
            ui.txtIdCliente.setText("");
            verTelefonos();
            ui.txtIdCliente.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
            ui.tblTelefonos.changeSelection(ui.tblTelefonos.getRowCount() - 1, 0, false, false);
        } else {
            ui.txtIdCliente.setToolTipText("Error al insertar el teléfono");
            ui.txtIdCliente.setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));

        }
    }

    private static void verClientes() {
        ResultSet clientes = Manager.verClientes();
        try {
            Object[] columns = {"ID", "Nombre", "Apellido"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            while (clientes.next()) {
                Object[] row = {clientes.getInt("id"), clientes.getString("nombre"), clientes.getString("apellido")};
                model.addRow(row);
            }
            ui.tblClientes.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void verTelefonos(){
        ResultSet telefonos = Manager.verTelefonos();
        try {
            Object[] columns = {"ID", "Número", "ClienteID"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            while (telefonos.next()) {
                Object[] row = {telefonos.getInt("id"), telefonos.getString("numero"), telefonos.getInt("Cliente_id")};
                model.addRow(row);
            }
            ui.tblTelefonos.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}