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

@WebServlet("/admin-consultas")
public class AdminConsultasServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao = request.getSession(false);

        if (sessao == null
                || !Boolean.TRUE.equals(sessao.getAttribute("admin"))) {
            response.sendRedirect(request.getContextPath() + "/entrar.jsp");
            return;
        }

        String acao = valor(request, "acao");
        String mensagem;

        try {
            int idConsulta = Integer.parseInt(valor(request, "idConsulta"));

            if ("excluir".equals(acao)) {
                mensagem = excluir(idConsulta);

            } else if ("editar".equals(acao)) {
                String data = valor(request, "dataConsulta");
                String horario = valor(request, "horario");

                if (!data.matches("\\d{2}/\\d{2}")
                        || !horario.matches("\\d{2}:\\d{2}")) {
                    throw new IllegalArgumentException(
                            "Use o formato dd/mm e hh:mm."
                    );
                }

                mensagem = editar(idConsulta, data, horario);

            } else {
                throw new IllegalArgumentException("Operação inválida.");
            }

        } catch (NumberFormatException e) {
            mensagem = "Consulta inválida.";

        } catch (IllegalArgumentException e) {
            mensagem = e.getMessage();

        } catch (Exception e) {
            getServletContext().log("Erro na administração de consultas", e);
            mensagem = "Não foi possível concluir a operação.";
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/admin.jsp?msg="
                        + URLEncoder.encode(mensagem, StandardCharsets.UTF_8)
        );
    }

    private String valor(HttpServletRequest request, String campo) {
        String valor = request.getParameter(campo);
        return valor == null ? "" : valor.trim();
    }

    private String excluir(int idConsulta) throws Exception {
        String sql = "DELETE FROM consultas WHERE id_consulta = ?";

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setInt(1, idConsulta);

            return comando.executeUpdate() > 0
                    ? "Consulta excluída."
                    : "Consulta não encontrada.";

        } catch (SQLException e) {
            return "Não foi possível excluir a consulta. "
                    + "Ela pode ter acompanhamentos vinculados.";
        }
    }

    private String editar(int idConsulta, String data, String horario)
            throws Exception {

        String sql = "UPDATE consultas "
                + "SET data_consulta = ?, horario = ? "
                + "WHERE id_consulta = ?";

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, data);
            comando.setString(2, horario);
            comando.setInt(3, idConsulta);

            return comando.executeUpdate() > 0
                    ? "Consulta alterada."
                    : "Consulta não encontrada.";
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/admin.jsp");
    }
}