package br.com.cannamed.dao;

import br.com.cannamed.model.Medico;
import java.sql.*;

public class MedicoDAO {

    public void cadastrar(Connection conexao, Medico medico) throws SQLException {
        String sql = """
            INSERT INTO medicos (id_usuario, especialidade)
            VALUES (?, ?)
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, medico.getIdUsuario());
            stmt.setString(2, medico.getEspecialidade());

            stmt.executeUpdate();
        }
    }
}
