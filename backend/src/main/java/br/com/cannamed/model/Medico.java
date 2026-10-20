package br.com.cannamed.model;

public class Medico {
    private int idMedico;
    private int idUsuario;
    private String especialidade;



    public Medico(int idUsuario, String especialidade) {
        this.idUsuario = idUsuario;
        this.especialidade = especialidade;
    }

   
    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}