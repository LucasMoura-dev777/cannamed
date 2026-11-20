<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.com.cannamed.util.Conexao" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%!
    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    if (!Boolean.TRUE.equals(session.getAttribute("admin"))) {
        response.sendRedirect(request.getContextPath() + "/entrar.jsp");
        return;
    }

    String totalPacientes = "-";
    String totalConsultas = "-";
    String totalUsuarios = "-";
    boolean erroAoCarregar = false;
    List<String[]> consultas = new ArrayList<>();
    List<String[]> usuarios = new ArrayList<>();

    String mensagem = request.getParameter("msg");

    String sqlTotais = "SELECT "
            + "(SELECT COUNT(*) FROM pacientes p "
            + "JOIN usuarios u ON u.id_usuario = p.id_usuario "
            + "WHERE UPPER(u.tipo_usuario) = 'PACIENTE') AS total_pacientes, "
            + "(SELECT COUNT(*) FROM consultas) AS total_consultas, "
            + "(SELECT COUNT(*) FROM usuarios) AS total_usuarios";

    String sqlConsultas = "SELECT c.id_consulta, u.nome, c.medico_nome, "
            + "c.data_consulta, c.horario "
            + "FROM consultas c "
            + "JOIN pacientes p ON p.id_paciente = c.id_paciente "
            + "JOIN usuarios u ON u.id_usuario = p.id_usuario "
            + "ORDER BY c.id_consulta DESC";

    String sqlUsuarios = "SELECT id_usuario, nome, email, "
            + "tipo_usuario, status "
            + "FROM usuarios "
            + "ORDER BY id_usuario";

    try (Connection conexao = Conexao.conectar()) {

        try (
                PreparedStatement comando = conexao.prepareStatement(sqlTotais);
                ResultSet resultado = comando.executeQuery()
        ) {
            if (resultado.next()) {
                totalPacientes = resultado.getString("total_pacientes");
                totalConsultas = resultado.getString("total_consultas");
                totalUsuarios = resultado.getString("total_usuarios");
            }
        }

        try (
                PreparedStatement comando = conexao.prepareStatement(sqlConsultas);
                ResultSet resultado = comando.executeQuery()
        ) {
            while (resultado.next()) {
                consultas.add(new String[] {
                        resultado.getString("id_consulta"),
                        resultado.getString("nome"),
                        resultado.getString("medico_nome"),
                        resultado.getString("data_consulta"),
                        resultado.getString("horario")
                });
            }
        }

        try (
                PreparedStatement comando = conexao.prepareStatement(sqlUsuarios);
                ResultSet resultado = comando.executeQuery()
        ) {
            while (resultado.next()) {
                usuarios.add(new String[] {
                        resultado.getString("id_usuario"),
                        resultado.getString("nome"),
                        resultado.getString("email"),
                        resultado.getString("tipo_usuario"),
                        resultado.getString("status")
                });
            }
        }

    } catch (Exception e) {
        application.log("Erro ao carregar o painel administrativo", e);
        erroAoCarregar = true;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>CANNAMED - Painel Administrativo</title>

    <link rel="stylesheet" href="style-admin.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link
        href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
        rel="stylesheet"
    >
</head>

<body>

<header class="navbar">
    <a href="index.jsp" class="logo">
        CANNAMED
    </a>

    <nav class="nav-links">
        <a href="index.jsp">Início</a>
        <a href="#consultas">Consultas</a>
        <a href="#usuarios">Usuários</a>
        <a href="#">Sobre</a>
        <a href="#">Profissionais</a>
    </nav>

    <div class="auth-buttons">
        <a href="sair" class="btn-login">Sair</a>
    </div>
</header>

<main class="admin-container">
    <div class="admin-wrapper">

        <div class="admin-header-title">
            <h1>Painel administrativo</h1>
            <p>Visão geral do sistema</p>
        </div>

        <% if (mensagem != null && !mensagem.isBlank()) { %>
            <p><%= escapar(mensagem) %></p>
        <% } %>

        <% if (erroAoCarregar) { %>
            <p>Não foi possível carregar os dados do painel.</p>
        <% } %>

        <div class="metrics-grid">

            <div class="metric-card">
                <span class="metric-label">Pacientes:</span>
                <span class="metric-value"><%= totalPacientes %></span>
            </div>

            <div class="metric-card">
                <span class="metric-label">Profissionais:</span>
                <span class="metric-value">4</span>
            </div>

            <div class="metric-card">
                <span class="metric-label">Consultas:</span>
                <span class="metric-value"><%= totalConsultas %></span>
            </div>

            <div class="metric-card">
                <span class="metric-label">Usuários:</span>
                <span class="metric-value"><%= totalUsuarios %></span>
            </div>

        </div>

        <div class="consultas-panel-card" id="consultas">
            <h2>Consultas agendadas:</h2>

            <% if (consultas.isEmpty()) { %>

                <p>Nenhuma consulta agendada.</p>

            <% } else { %>

                <table style="width: 100%; border-collapse: collapse; margin-top: 15px;">
                    <thead>
                        <tr style="text-align: left; border-bottom: 2px solid #0d3b2e;">
                            <th style="padding: 8px;">Paciente</th>
                            <th style="padding: 8px;">Profissional</th>
                            <th style="padding: 8px;">Data, horário e ações</th>
                        </tr>
                    </thead>

                    <tbody>
                        <% for (String[] consulta : consultas) { %>
                            <tr style="border-bottom: 1px solid #d5e0db;">
                                <td style="padding: 8px;"><%= escapar(consulta[1]) %></td>
                                <td style="padding: 8px;"><%= escapar(consulta[2]) %></td>
                                <td style="padding: 8px;">
                                    <form
                                        action="<%= request.getContextPath() %>/admin-consultas"
                                        method="POST"
                                        style="display: flex; flex-wrap: wrap; gap: 6px; align-items: center; margin: 0;"
                                    >
                                        <input type="hidden" name="idConsulta" value="<%= escapar(consulta[0]) %>">

                                        <input
                                            type="text"
                                            name="dataConsulta"
                                            value="<%= escapar(consulta[3]) %>"
                                            maxlength="5"
                                            placeholder="dd/mm"
                                            required
                                            style="width: 80px;"
                                        >

                                        <input
                                            type="text"
                                            name="horario"
                                            value="<%= escapar(consulta[4]) %>"
                                            maxlength="5"
                                            placeholder="hh:mm"
                                            required
                                            style="width: 70px;"
                                        >

                                        <button type="submit" name="acao" value="editar">
                                            Salvar
                                        </button>

                                        <button
                                            type="submit"
                                            name="acao"
                                            value="excluir"
                                            onclick="return confirm('Tem certeza que deseja excluir esta consulta?')"
                                        >
                                            Excluir
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

            <% } %>
        </div>

        <div class="consultas-panel-card" id="usuarios" style="margin-top: 25px;">
            <h2>Usuários cadastrados:</h2>

            <% if (usuarios.isEmpty()) { %>

                <p>Nenhum usuário cadastrado.</p>

            <% } else { %>

                <table style="width: 100%; border-collapse: collapse; margin-top: 15px;">
                    <thead>
                        <tr style="text-align: left; border-bottom: 2px solid #0d3b2e;">
                            <th style="padding: 8px;">ID</th>
                            <th style="padding: 8px;">Nome</th>
                            <th style="padding: 8px;">E-mail</th>
                            <th style="padding: 8px;">Tipo</th>
                            <th style="padding: 8px;">Situação</th>
                        </tr>
                    </thead>

                    <tbody>
                        <% for (String[] usuario : usuarios) { %>
                            <tr style="border-bottom: 1px solid #d5e0db;">
                                <td style="padding: 8px;"><%= escapar(usuario[0]) %></td>
                                <td style="padding: 8px;"><%= escapar(usuario[1]) %></td>
                                <td style="padding: 8px;"><%= escapar(usuario[2]) %></td>
                                <td style="padding: 8px;"><%= escapar(usuario[3]) %></td>
                                <td style="padding: 8px;"><%= escapar(usuario[4]) %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

            <% } %>
        </div>

    </div>
</main>

<p style="text-align: center; padding: 25px;">
    <a href="pacientes"
       style="display: inline-block;
              background-color: #0d3b2e;
              color: #ffffff;
              padding: 12px 24px;
              border-radius: 8px;
              text-decoration: none;
              font-weight: 600;">
        Gerenciar pacientes
    </a>
</p>

</body>
</html>