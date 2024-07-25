package com.mycompany.transacciones;

public class Telefono {
    private int id;
    private String numero;
    private int Cliente_id;

    // Constructor
    public Telefono(int id, String numero, int Cliente_id) {
        this.id = id;
        this.numero = numero;
        this.Cliente_id = Cliente_id;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getClienteId() {
        return Cliente_id;
    }

    public void setClienteId(int Cliente_id) {
        this.Cliente_id = Cliente_id;
    }
}

