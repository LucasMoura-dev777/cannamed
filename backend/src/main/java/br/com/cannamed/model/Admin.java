package br.com.cannamed.model;

public class Admin {
    private int idAdmin;
    private int idUsuario;
    private String cargo;

    public Admin() {
    }

    public Admin(int idUsuario, String cargo) {
        this.idUsuario = idUsuario;
        this.cargo = cargo;
    }

    // Getters e Setters
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}