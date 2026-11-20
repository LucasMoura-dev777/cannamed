package br.com.cannamed.controller;

import br.com.cannamed.util.InicializadorBanco;
import br.com.cannamed.util.Conexao;
import br.com.cannamed.service.CadastroPacienteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(
        urlPatterns = {"/pacientes", "/cadastrar-paciente"},
        loadOnStartup = 1
)
public class PacientesServlet extends HttpServlet {

 @Override
 public void init() throws ServletException {
  try {
   InicializadorBanco.inicializar();
  } catch (Exception e) {
   throw new ServletException(e);
  }
 }

 private String p(HttpServletRequest request, String campo) {
  String valor = request.getParameter(campo);
  return valor == null ? "" : valor.trim();
 }

 private void redirect(
         HttpServletResponse response,
         String pagina,
         String mensagem
 ) throws IOException {

  response.sendRedirect(
          pagina + "?msg="
                  + URLEncoder.encode(
                  mensagem,
                  StandardCharsets.UTF_8
          )
  );
 }

 private boolean admin(HttpServletRequest request) {
  return request.getSession(false) != null
          && Boolean.TRUE.equals(
          request.getSession(false).getAttribute("admin")
  );
 }

 @Override
 protected void doGet(
         HttpServletRequest request,
         HttpServletResponse response
 ) throws IOException, ServletException {

  if (!admin(request)) {
   response.sendError(
           403,
           "Área administrativa: entre com uma conta administradora."
   );
   return;
  }

  request.setAttribute("records", records());

  request.getRequestDispatcher("/pacientes.jsp")
          .forward(request, response);
 }

 private List<String[]> records() throws ServletException {
  List<String[]> lista = new ArrayList<>();

  String sql = "SELECT p.id_paciente, u.nome, u.email, "
          + "p.cpf, p.data_nascimento "
          + "FROM pacientes p "
          + "JOIN usuarios u ON u.id_usuario = p.id_usuario "
          + "WHERE UPPER(u.tipo_usuario) = 'PACIENTE' "
          + "ORDER BY p.id_paciente DESC";

  try (
          Connection conexao = Conexao.conectar();
          PreparedStatement comando = conexao.prepareStatement(sql);
          ResultSet resultado = comando.executeQuery()
  ) {
   while (resultado.next()) {
    lista.add(new String[] {
            resultado.getString(1),
            resultado.getString(2),
            resultado.getString(3),
            resultado.getString(4),
            resultado.getString(5)
    });
   }

  } catch (SQLException e) {
   throw new ServletException(e);
  }

  return lista;
 }

 @Override
 protected void doPost(
         HttpServletRequest request,
         HttpServletResponse response
 ) throws IOException, ServletException {

  request.setCharacterEncoding("UTF-8");

  String acao = p(request, "acao");

  if (request.getServletPath().equals("/cadastrar-paciente")) {
   acao = "criar";
  }

  try {
   if ("criar".equals(acao)) {
    if (!p(request, "senha").equals(
            p(request, "confirmarSenha")
    )) {
     throw new IllegalArgumentException("Senhas diferentes");
    }

    new CadastroPacienteService().cadastrar(
            p(request, "nome"),
            p(request, "email"),
            p(request, "senha"),
            p(request, "cpf").replaceAll("\\D", ""),
            LocalDate.parse(p(request, "dataNascimento"))
    );

    redirect(
            response,
            "entrar.jsp",
            "Cadastro realizado! Entre com sua conta."
    );
    return;
   }

   if (!admin(request)) {
    response.sendError(
            403,
            "Somente administradores podem alterar pacientes"
    );
    return;
   }

   int id = Integer.parseInt(p(request, "id"));

   try (Connection conexao = Conexao.conectar()) {
    conexao.setAutoCommit(false);

    try {
     // Confirma que o registro pertence a um paciente,
     // e não a uma conta de administrador.
     String verificarPaciente = "SELECT p.id_paciente "
             + "FROM pacientes p "
             + "JOIN usuarios u "
             + "ON u.id_usuario = p.id_usuario "
             + "WHERE p.id_paciente = ? "
             + "AND UPPER(u.tipo_usuario) = 'PACIENTE'";

     try (PreparedStatement comando =
                  conexao.prepareStatement(verificarPaciente)) {

      comando.setInt(1, id);

      try (ResultSet resultado = comando.executeQuery()) {
       if (!resultado.next()) {
        throw new IllegalArgumentException(
                "Paciente não encontrado."
        );
       }
      }
     }

     if ("editar".equals(acao)) {
      String nome = p(request, "nome");
      String email = p(request, "email");

      String cpf = p(request, "cpf")
              .replaceAll("\\D", "");

      LocalDate nascimento = LocalDate.parse(
              p(request, "dataNascimento")
      );

      if (nome.isBlank()
              || !email.contains("@")
              || !cpf.matches("\\d{11}")
              || nascimento.isAfter(LocalDate.now())) {

       throw new IllegalArgumentException(
               "Dados inválidos"
       );
      }

      String atualizarUsuario = "UPDATE usuarios "
              + "SET nome = ?, email = ? "
              + "WHERE id_usuario = "
              + "(SELECT id_usuario FROM pacientes "
              + "WHERE id_paciente = ?)";

      try (PreparedStatement comando =
                   conexao.prepareStatement(atualizarUsuario)) {

       comando.setString(1, nome);
       comando.setString(2, email);
       comando.setInt(3, id);

       if (comando.executeUpdate() != 1) {
        throw new SQLException(
                "Paciente não encontrado"
        );
       }
      }

      String atualizarPaciente = "UPDATE pacientes "
              + "SET cpf = ?, data_nascimento = ? "
              + "WHERE id_paciente = ?";

      try (PreparedStatement comando =
                   conexao.prepareStatement(atualizarPaciente)) {

       comando.setString(1, cpf);
       comando.setDate(2, Date.valueOf(nascimento));
       comando.setInt(3, id);
       comando.executeUpdate();
      }

     } else if ("excluir".equals(acao)) {
      int idUsuario;

      try (PreparedStatement comando =
                   conexao.prepareStatement(
                           "SELECT id_usuario FROM pacientes "
                                   + "WHERE id_paciente = ?"
                   )) {

       comando.setInt(1, id);

       try (ResultSet resultado = comando.executeQuery()) {
        if (!resultado.next()) {
         throw new SQLException(
                 "Paciente não encontrado"
         );
        }

        idUsuario = resultado.getInt(1);
       }
      }

      try (PreparedStatement comando =
                   conexao.prepareStatement(
                           "SELECT COUNT(*) FROM consultas "
                                   + "WHERE id_paciente = ?"
                   )) {

       comando.setInt(1, id);

       try (ResultSet resultado = comando.executeQuery()) {
        resultado.next();

        if (resultado.getInt(1) > 0) {
         throw new IllegalArgumentException(
                 "Paciente possui consultas. "
                         + "Cancele/remova os vínculos "
                         + "antes de excluir."
         );
        }
       }
      }

      try (PreparedStatement comando =
                   conexao.prepareStatement(
                           "DELETE FROM pacientes "
                                   + "WHERE id_paciente = ?"
                   )) {

       comando.setInt(1, id);
       comando.executeUpdate();
      }

      try (PreparedStatement comando =
                   conexao.prepareStatement(
                           "DELETE FROM usuarios "
                                   + "WHERE id_usuario = ?"
                   )) {

       comando.setInt(1, idUsuario);
       comando.executeUpdate();
      }

     } else {
      throw new IllegalArgumentException(
              "Operação inválida"
      );
     }

     conexao.commit();

    } catch (Exception e) {
     conexao.rollback();
     throw e;
    }
   }

   redirect(response, "pacientes", "Operação concluída");

  } catch (Exception e) {
   String mensagem = e.getMessage() == null
           ? "Erro no cadastro"
           : e.getMessage();

   redirect(response, "erro.jsp", mensagem);
  }
 }
}