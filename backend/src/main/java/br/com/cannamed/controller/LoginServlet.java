package br.com.cannamed.controller;

import br.com.cannamed.util.Conexao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login-paciente")
public class LoginServlet extends HttpServlet {

 @Override
 protected void doPost(
         HttpServletRequest request,
         HttpServletResponse response
 ) throws IOException {

  request.setCharacterEncoding("UTF-8");

  String email = request.getParameter("email");
  String senha = request.getParameter("senha");

  if (email == null || senha == null) {
   response.sendRedirect("entrar.jsp?erro=1");
   return;
  }

  String sql = """
                SELECT id_usuario, nome, senha, tipo_usuario
                FROM usuarios
                WHERE email = ? AND status = 'ATIVO'
                """;

  try (
          Connection conexao = Conexao.conectar();
          PreparedStatement comando = conexao.prepareStatement(sql)
  ) {
   comando.setString(1, email.trim());

   try (ResultSet resultado = comando.executeQuery()) {

    if (
            resultado.next()
                    && resultado.getString("senha").startsWith("$2")
                    && BCrypt.checkpw(
                    senha,
                    resultado.getString("senha")
            )
    ) {
     HttpSession sessaoAnterior = request.getSession(false);

     if (sessaoAnterior != null) {
      sessaoAnterior.invalidate();
     }

     HttpSession sessao = request.getSession(true);

     boolean admin = "ADMIN".equalsIgnoreCase(
             resultado.getString("tipo_usuario")
     );

     sessao.setAttribute(
             "uid",
             resultado.getInt("id_usuario")
     );

     sessao.setAttribute(
             "nome",
             resultado.getString("nome")
     );

     sessao.setAttribute("admin", admin);

     if (admin) {
      response.sendRedirect("admin.jsp");
     } else {
      response.sendRedirect("index.jsp");
     }

     return;
    }
   }

  } catch (Exception e) {
   getServletContext().log("Falha no login", e);
  }

  response.sendRedirect("entrar.jsp?erro=1");
 }
}