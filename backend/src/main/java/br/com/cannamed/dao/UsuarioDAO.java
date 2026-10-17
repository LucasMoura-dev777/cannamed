
package br.com.cannamed.dao;

import br.com.cannamed.model.Usuario;

import java.sql.*;

public class UsuarioDAO {

    public int cadastrar(Connection conexao, Usuario usuario)
            throws SQLException {

        String sql = """
            INSERT INTO usuarios
            (nome, email, senha, telefone, tipo_usuario, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conexao.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getTipoUsuario());
            stmt.setString(6, usuario.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("ID do usuário não gerado.");
        }
    }
}
