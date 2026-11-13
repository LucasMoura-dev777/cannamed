package br.com.cannamed.controller;

import br.com.cannamed.util.Conexao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/cancelar-consulta")
public class CancelarConsultaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String idConsultaStr = request.getParameter("idConsulta");

        if (idConsultaStr != null && !idConsultaStr.isBlank()) {
            try {
                int idConsulta = Integer.parseInt(idConsultaStr);

                String sql = "DELETE FROM consultas WHERE id_consulta = ?";

                try (Connection conexao = Conexao.conectar();
                     PreparedStatement stmt = conexao.prepareStatement(sql)) {

                    stmt.setInt(1, idConsulta);
                    stmt.executeUpdate();

                    System.out.println("Consulta com ID " + idConsulta + " cancelada e removida do banco com sucesso!");
                }

            } catch (SQLException | NumberFormatException e) {
                getServletContext().log("Erro ao cancelar consulta", e);
                // Redireciona para a página de erro caso ocorra algum problema com a base de dados
                response.sendRedirect("erro.jsp?msg=" + URLEncoder.encode("Erro ao cancelar a consulta. Tente novamente.", StandardCharsets.UTF_8));
                return;
            }
        }


        response.sendRedirect("minhas-consultas.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.sendRedirect("minhas-consultas.jsp");
    }
}