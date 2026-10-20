package br.com.cannamed.dao;

import br.com.cannamed.model.Admin;
import java.sql.*;

public class AdminDAO {

    public void cadastrar(Connection conexao, Admin admin) throws SQLException {
        String sql = """
            INSERT INTO admin (id_usuario, cargo)
            VALUES (?, ?)
        """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, admin.getIdUsuario());
            stmt.setString(2, admin.getCargo());

            stmt.executeUpdate();
        }
    }
}