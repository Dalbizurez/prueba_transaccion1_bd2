/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.transacciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Massielle
 */
public class TransaccionesPostgres {

    private static final UI ui = new UI();

    public static void main(String[] args) {

        setUpUi();

    }

    private static void setUpUi() {
        ui.setVisible(true);
        ui.btnCommit.setEnabled(false);
        ui.btnRollback.setEnabled(false);
        ui.btnGuardar.setEnabled(false);
        ui.btnTelefono.setEnabled(false);
        ui.jButton1.setEnabled(false);
        ui.jButton2.setEnabled(false);
        ui.btn_delete.setEnabled(false);

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
            ui.jButton1.setEnabled(true);
            ui.jButton2.setEnabled(true);
            ui.btn_delete.setEnabled(true);
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
            ui.btn_delete.setEnabled(false);
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
            ui.btn_delete.setEnabled(false);
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
        ui.btn_delete.addActionListener(e -> {
            borrarCliente();
        });

        ui.btnActualizar.addActionListener(e -> {
            verClientes();
            verTelefonos();
        });

        ui.tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleClienteSelection();
            }
        });

        ui.jButton1.addActionListener(e -> actualizarCliente());

        ui.tblTelefonos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTelefonoSelection();
            }
        });

        ui.jButton2.addActionListener(e -> actualizarTelefono());

        verClientes();
        verTelefonos();

    }

    private static void handleClienteSelection() {
        int selectedRow = ui.tblClientes.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(ui.tblClientes.getValueAt(selectedRow, 0).toString());
            String nombre = ui.tblClientes.getValueAt(selectedRow, 1).toString();
            String apellido = ui.tblClientes.getValueAt(selectedRow, 2).toString();

            ui.txtIdCliente.setText(String.valueOf(id));
            ui.txtNombre.setText(nombre);
            ui.txtApellido.setText(apellido);

        }
    }

    private static void actualizarCliente() {
        try {
            int id = Integer.parseInt(ui.txtIdCliente.getText());
            String nombre = ui.txtNombre.getText();
            String apellido = ui.txtApellido.getText();
            String direccion = ui.txtDireccion.getText();

            if (ManagerPostgres.actualizarCliente(id, nombre, apellido, direccion)) {
                System.out.println("Cliente actualizado exitosamente");
                ui.txtIdCliente.setText("");
                ui.txtNombre.setText("");
                ui.txtApellido.setText("");
                verClientes();
            } else {
                System.out.println("Error al actualizar el cliente");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }

    }

    private static void borrarCliente() {
        try {
            int id = Integer.parseInt(ui.txtIdCliente.getText());
            if (ManagerPostgres.borrarTelefono(id) && ManagerPostgres.borrarCliente(id)) {
                System.out.println("Cliente eliminado");
                ui.txtIdCliente.setText("");
                ui.txtNombre.setText("");
                ui.txtApellido.setText("");
                verClientes();
            } else {
                System.out.println("Error al eliminar el cliente");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido");
        }
    }

    private static void handleTelefonoSelection() {
        int selectedRow = ui.tblTelefonos.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(ui.tblTelefonos.getValueAt(selectedRow, 0).toString());
            String telefono = ui.tblTelefonos.getValueAt(selectedRow, 1).toString();

            ui.txtIdCliente.setText(String.valueOf(id));
            ui.txtTelefono.setText(telefono);

        }
    }

    private static void actualizarTelefono() {
        try {
            // Obtener el ID del teléfono directamente desde la selección de la tabla
            int idTelefono = Integer.parseInt(ui.tblTelefonos.getValueAt(ui.tblTelefonos.getSelectedRow(), 0).toString());
            String numero = ui.txtTelefono.getText();
            int clienteId = Integer.parseInt(ui.txtIdCliente.getText());

            if (ManagerPostgres.existeCliente(clienteId)) {
                if (ManagerPostgres.actualizarTelefono(idTelefono, numero, clienteId)) {
                    ui.txtTelefono.setText("");
                    ui.txtIdCliente.setText("");

                    verTelefonos();
                } else {
                    ui.txtTelefono.setToolTipText("Error al actualizar el teléfono");
                }
            } else {
                ui.txtIdCliente.setToolTipText("ID de cliente inválido");
            }
        } catch (NumberFormatException e) {
            ui.txtIdCliente.setToolTipText("ID inválido");
        }
    }

    private static void insertarCliente(String nombre, String apellido, String direccion) {
        if (ManagerPostgres.insertarCliente(nombre, apellido, direccion)) {
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
        if (ManagerPostgres.insertarTelefono(numero, clienteId)) {
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
        ResultSet clientes = ManagerPostgres.verClientes();
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

    private static void verTelefonos() {
        ResultSet telefonos = ManagerPostgres.verTelefonos();
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

    private static void commit() {
        ManagerPostgres.commit();
        verClientes();
        verTelefonos();
    }

    private static void rollback() {
        ManagerPostgres.rollback();
        verClientes();
        verTelefonos();
    }

    private static void begin() {
        int level = 4;
        if (ui.buttonGroup1.isSelected(ui.radio_uncommitted.getModel())) {
            level = 1;
        } else if (ui.buttonGroup1.isSelected(ui.radio_committed.getModel())) {
            level = 2;
        } else if (ui.buttonGroup1.isSelected(ui.radio_serializable.getModel())) {
            level = 8;
        }
        ManagerPostgres.start(level);
    }

    public static void exportDatabase(String filePath) {
        try {
            String dumpCommand = "pg_dump -U postgres -h localhost -d transacciones -f " + filePath;

            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", dumpCommand);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Exportación completada con éxito.");
            } else {
                System.err.println("Error en la exportación. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void importDatabase(String filePath) {
        try {
            String importCommand = "psql -U postgres -h localhost -d transacciones -f " + filePath;
    
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", importCommand);
            processBuilder.redirectErrorStream(true);
    
            Process process = processBuilder.start();
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
    
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Importación completada con éxito.");
            } else {
                System.err.println("Error en la importación. Código de salida: " + exitCode);
            }
    
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteDatabase() {
        try {
            String dropCommand = "dropdb -U [postgres] -h localhost transacciones";
    
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", dropCommand);
            processBuilder.redirectErrorStream(true);
    
            Process process = processBuilder.start();
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
    
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Base de datos eliminada con éxito.");
            } else {
                System.err.println("Error al eliminar la base de datos. Código de salida: " + exitCode);
            }
    
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }    
}
