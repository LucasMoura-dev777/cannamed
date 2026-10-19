package br.com.cannamed.model;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String tipoUsuario;
    private String status;

    public Usuario(String nome, String email, String senha, String telefone, String tipoUsuario, String status){

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public String getStatus() {
        return status;
    }
}

