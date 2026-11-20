package br.com.cannamed.controller;

import br.com.cannamed.util.Conexao;
import br.com.cannamed.util.InicializadorBanco;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class AdminBootstrap implements ServletContextListener {

 @Override
 public void contextInitialized(ServletContextEvent event) {
  InicializadorBanco.inicializar();

  criarAdmin("Matheus", "matheusm@admin.com", "admin123");
  criarAdmin("Daniel", "danielp@admin.com", "admin123");
  criarAdmin("Thiago", "thiagof@admin.com", "admin123");
  criarAdmin("Lucas", "lucasm@admin.com", "admin123");

  criarMedico("Dr. Thiago Felipe", "thiago@cannamed.com", "CRM123");
 }

 private void criarAdmin(String nome, String email, String senha) {
  String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

  String sql = """
                INSERT INTO usuarios
                (nome, email, senha, tipo_usuario, status)
                SELECT ?, ?, ?, 'ADMIN', 'ATIVO'
                WHERE NOT EXISTS (
                    SELECT 1 FROM usuarios WHERE email = ?
                )
                """;

  try (
          Connection conexao = Conexao.conectar();
          PreparedStatement comando = conexao.prepareStatement(sql)
  ) {
   comando.setString(1, nome);
   comando.setString(2, email);
   comando.setString(3, senhaCriptografada);
   comando.setString(4, email);

   comando.executeUpdate();

  } catch (SQLException e) {
   throw new IllegalStateException(
           "Erro ao criar administrador: " + email,
           e
   );
  }
 }

 private void criarMedico(String nome, String email, String registro) {
  try (Connection conexao = Conexao.conectar()) {

   try (PreparedStatement comando = conexao.prepareStatement(
           "SELECT id_profissional FROM profissionais "
                   + "WHERE registro_profissional = ?"
   )) {
    comando.setString(1, registro);

    try (ResultSet resultado = comando.executeQuery()) {
     if (resultado.next()) {
      return;
     }
    }
   }

   int idEspecialidade = 0;

   try (PreparedStatement comando = conexao.prepareStatement(
           "SELECT id_especialidade FROM especialidades LIMIT 1"
   ); ResultSet resultado = comando.executeQuery()) {

    if (resultado.next()) {
     idEspecialidade = resultado.getInt(1);
    }
   }

   if (idEspecialidade == 0) {
    try (PreparedStatement comando = conexao.prepareStatement(
            "INSERT INTO especialidades (nome, descricao) "
                    + "VALUES ('Cannabis Medicinal', "
                    + "'Tratamentos com canabinoides')",
            Statement.RETURN_GENERATED_KEYS
    )) {
     comando.executeUpdate();

     try (ResultSet resultado = comando.getGeneratedKeys()) {
      resultado.next();
      idEspecialidade = resultado.getInt(1);
     }
    }
   }

   int idUsuario = 0;

   try (PreparedStatement comando = conexao.prepareStatement(
           "SELECT id_usuario FROM usuarios WHERE email = ?"
   )) {
    comando.setString(1, email);

    try (ResultSet resultado = comando.executeQuery()) {
     if (resultado.next()) {
      idUsuario = resultado.getInt(1);
     }
    }
   }

   if (idUsuario == 0) {
    try (PreparedStatement comando = conexao.prepareStatement(
            "INSERT INTO usuarios "
                    + "(nome, email, senha, tipo_usuario, status) "
                    + "VALUES (?, ?, ?, 'MEDICO', 'ATIVO')",
            Statement.RETURN_GENERATED_KEYS
    )) {
     comando.setString(1, nome);
     comando.setString(2, email);
     comando.setString(3, BCrypt.hashpw("123", BCrypt.gensalt()));
     comando.executeUpdate();

     try (ResultSet resultado = comando.getGeneratedKeys()) {
      resultado.next();
      idUsuario = resultado.getInt(1);
     }
    }
   }

   try (PreparedStatement comando = conexao.prepareStatement(
           "INSERT INTO profissionais "
                   + "(id_usuario, registro_profissional, descricao, id_especialidade) "
                   + "VALUES (?, ?, 'Médico especialista', ?)"
   )) {
    comando.setInt(1, idUsuario);
    comando.setString(2, registro);
    comando.setInt(3, idEspecialidade);
    comando.executeUpdate();
   }

  } catch (SQLException e) {
   throw new IllegalStateException(
           "Erro ao criar médico: " + email,
           e
   );
  }
 }
}