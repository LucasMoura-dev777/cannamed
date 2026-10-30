
package br.com.cannamed.service;

import br.com.cannamed.dao.UsuarioDAO;
import br.com.cannamed.dao.PacienteDAO;
import br.com.cannamed.model.Usuario;
import br.com.cannamed.model.Paciente;
import br.com.cannamed.util.Conexao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

public class CadastroPacienteService {

    public void cadastrar(
            String nome,
            String email,
            String senha,
            String cpf,
            LocalDate nascimento) throws SQLException {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.length() < 8
                || cpf == null || !cpf.matches("\\d{11}")
                || nascimento == null
                || nascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Dados de cadastro inválidos.");
        }

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {
                Usuario usuario = new Usuario(
                        nome,
                        email,
                        BCrypt.hashpw(senha, BCrypt.gensalt(10)),
                        null,
                        "PACIENTE",
                        "ATIVO"
                );

                UsuarioDAO usuarioDAO = new UsuarioDAO();

                int idUsuario = usuarioDAO.cadastrar(
                        conexao, usuario);

                Paciente paciente = new Paciente(
                        idUsuario,
                        cpf,
                        nascimento
                );

                PacienteDAO pacienteDAO = new PacienteDAO();

                pacienteDAO.cadastrar(conexao, paciente);

                conexao.commit();

                System.out.println(
                        "Cadastro completo realizado com sucesso!");

            } catch (Exception e) {
                conexao.rollback();
                throw e;
            }
        }
    }
}
