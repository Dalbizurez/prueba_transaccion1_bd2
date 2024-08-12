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
        ui.btnCommit.setEnabled(false);
        ui.btnRollback.setEnabled(false);
        ui.btnGuardar.setEnabled(false);
        ui.btnTelefono.setEnabled(false);
        
        ui.buttonGroup1.setSelected(ui.radio_repeat.getModel(), true);
                
        ui.btnIniciar.addActionListener(e -> {
            ui.btnGuardar.setEnabled(true);
            ui.btnTelefono.setEnabled(true);
            ui.btnCommit.setEnabled(true);
            ui.btnRollback.setEnabled(true);
            ui.radio_committed.setEnabled(false);
            ui.radio_uncommitted.setEnabled(false);
            ui.radio_repeat.setEnabled(false);
            ui.radio_serializable.setEnabled(false);
            begin();
        });
        ui.btnCommit.addActionListener(e -> {
            ui.btnCommit.setEnabled(false);
            ui.btnRollback.setEnabled(false);
            ui.btnGuardar.setEnabled(false);
            ui.btnTelefono.setEnabled(false);
            ui.radio_committed.setEnabled(true);
            ui.radio_uncommitted.setEnabled(true);
            ui.radio_repeat.setEnabled(true);
            ui.radio_serializable.setEnabled(true);
            ui.btnIniciar.setEnabled(true);
            commit();
        });
        ui.btnRollback.addActionListener(e -> {
            ui.btnCommit.setEnabled(false);
            ui.btnRollback.setEnabled(false);
            ui.btnGuardar.setEnabled(false);
            ui.btnTelefono.setEnabled(false);
            ui.radio_committed.setEnabled(true);
            ui.radio_uncommitted.setEnabled(true);
            ui.radio_repeat.setEnabled(true);
            ui.radio_serializable.setEnabled(true);
            ui.btnIniciar.setEnabled(true);
            rollback();
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
            ui.btnIniciar.setEnabled(false);
        });
        
        ui.btnActualizar.addActionListener(e -> {
            verClientes();
            verTelefonos();
        });
        
        verClientes();
        verTelefonos();

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

    private static void commit(){
        Manager.commit();
        verClientes();
        verTelefonos();
    }

    private static void rollback(){
        Manager.rollback();
        verClientes();
        verTelefonos();
    }
    
    private static void begin(){
        int level = 4;
        if (ui.buttonGroup1.isSelected(ui.radio_uncommitted.getModel())){
            level = 1;
        } else if (ui.buttonGroup1.isSelected(ui.radio_committed.getModel())) {
            level = 2;
        }  else if (ui.buttonGroup1.isSelected(ui.radio_serializable.getModel())) {
            level = 8;
        }
        Manager.start(level);
    }
}