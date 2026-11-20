package br.com.cannamed.controller;

import br.com.cannamed.util.Conexao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/cancelar-consulta")
public class CancelarConsultaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        Integer idUsuario = session == null
                ? null
                : (Integer) session.getAttribute("uid");

        if (idUsuario == null) {
            response.sendRedirect(
                    request.getContextPath() + "/entrar.jsp"
            );
            return;
        }

        String idConsultaStr = request.getParameter("idConsulta");

        if (idConsultaStr == null || idConsultaStr.isBlank()) {
            redirecionarErro(
                    request,
                    response,
                    "Selecione uma consulta para cancelar."
            );
            return;
        }

        try {
            int idConsulta = Integer.parseInt(idConsultaStr);

            String sql = """
                    DELETE FROM consultas
                    WHERE id_consulta = ?
                    AND id_paciente IN (
                        SELECT id_paciente
                        FROM pacientes
                        WHERE id_usuario = ?
                    )
                    """;

            int quantidadeExcluida;

            try (
                    Connection conexao = Conexao.conectar();
                    PreparedStatement comando =
                            conexao.prepareStatement(sql)
            ) {
                comando.setInt(1, idConsulta);
                comando.setInt(2, idUsuario);

                quantidadeExcluida = comando.executeUpdate();
            }

            if (quantidadeExcluida == 0) {
                redirecionarErro(
                        request,
                        response,
                        "A consulta não foi encontrada na sua conta."
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/minhas-consultas.jsp?msg=cancelada"
            );

        } catch (SQLException | NumberFormatException e) {
            getServletContext().log("Erro ao cancelar consulta", e);

            redirecionarErro(
                    request,
                    response,
                    "Não foi possível cancelar a consulta. "
                            + "Ela pode possuir acompanhamentos vinculados."
            );
        }
    }

    private void redirecionarErro(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensagem
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/erro.jsp?msg="
                        + URLEncoder.encode(
                        mensagem,
                        StandardCharsets.UTF_8
                )
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/minhas-consultas.jsp"
        );
    }
}