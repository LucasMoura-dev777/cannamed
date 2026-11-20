package br.com.cannamed.controller;

import br.com.cannamed.util.Conexao;
import br.com.cannamed.util.InicializadorBanco;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebListener
public class AdminBootstrap implements ServletContextListener {

 @Override
 public void contextInitialized(ServletContextEvent event) {
  InicializadorBanco.inicializar();

  criarAdmin(
          "Matheus",
          "matheusm@admin.com",
          "admin123"
  );

  criarAdmin(
          "Daniel",
          "danielp@admin.com",
          "admin123"
  );

  criarAdmin(
          "Thiago",
          "thiagof@admin.com",
          "admin123"
  );

  criarAdmin(
          "Lucas",
          "lucasm@admin.com",
          "admin123"
  );
 }

 private void criarAdmin(String nome, String email, String senha) {
  if (senha == null || senha.length() < 8) {
   System.out.println(
           "Senha não configurada para: " + email
   );
   return;
  }

  String senhaCriptografada = BCrypt.hashpw(
          senha,
          BCrypt.gensalt()
  );

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
}