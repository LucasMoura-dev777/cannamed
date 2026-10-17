
package br.com.cannamed.dao;

import br.com.cannamed.model.Paciente;

import java.sql.*;

public class PacienteDAO {

    public void cadastrar(Connection conexao, Paciente paciente)
            throws SQLException {

        String sql = """
            INSERT INTO pacientes
            (id_usuario, cpf, data_nascimento)
            VALUES (?, ?, ?)
            """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, paciente.getIdUsuario());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(
                    paciente.getDataNascimento()));

            stmt.executeUpdate();
        }
    }
}
