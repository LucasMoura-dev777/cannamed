package br.com.cannamed.model;

import java.time.LocalDate;

public class Paciente {

    private int idPaciente;
    private int idUsuario;
    private String cpf;
    private LocalDate dataNascimento;

    public Paciente(int idUsuario, String cpf,
                    LocalDate dataNascimento){
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
