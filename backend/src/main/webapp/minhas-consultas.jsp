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
    Integer idUsuario = (Integer) session.getAttribute("uid");

    if (idUsuario == null) {
        response.sendRedirect(request.getContextPath() + "/entrar.jsp");
        return;
    }

    List<String[]> consultas = new ArrayList<>();
    boolean erroAoCarregar = false;

  String sql = "SELECT c.id_consulta, "
          + "c.medico_nome, "
          + "c.medico_iniciais, "
          + "c.data_consulta, "
          + "c.horario "
          + "FROM consultas c "
          + "INNER JOIN pacientes p "
          + "ON p.id_paciente = c.id_paciente "
          + "WHERE p.id_usuario = ? "
          + "ORDER BY c.id_consulta DESC";

    try (
            Connection conexao = Conexao.conectar();
            PreparedStatement comando = conexao.prepareStatement(sql)
    ) {
        comando.setInt(1, idUsuario);

        try (ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                consultas.add(new String[] {
                        resultado.getString("id_consulta"),
                        resultado.getString("medico_nome"),
                        resultado.getString("medico_iniciais"),
                        resultado.getString("data_consulta"),
                        resultado.getString("horario")
                });
            }
        }

    } catch (Exception e) {
        application.log("Erro ao carregar consultas", e);
        erroAoCarregar = true;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>CANNAMED - Minhas Consultas</title>

    <link rel="stylesheet" href="style-consultas.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link
        href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
        rel="stylesheet"
    >
</head>

<body>

<header class="navbar">
    <div class="logo">
        CANNAMED
    </div>

    <nav class="nav-links">
        <a href="index.jsp">Início</a>
        <a href="minhas-consultas.jsp" class="active">Consultas</a>
        <a href="#">Sobre</a>
        <a href="#">Profissionais</a>
    </nav>

    <div class="auth-buttons">
        <a href="entrar.jsp" class="btn-login">Entrar</a>
        <a href="cadastro.jsp" class="btn-register-nav">Criar conta</a>
    </div>
</header>

<main class="main-container">
    <div class="consultas-wrapper">

        <h1 class="page-title">Minhas Consultas</h1>

        <% if ("cancelada".equals(request.getParameter("msg"))) { %>
            <p>Consulta cancelada com sucesso.</p>
        <% } %>

        <% if (erroAoCarregar) { %>

            <p>Não foi possível carregar suas consultas.</p>

        <% } else if (consultas.isEmpty()) { %>

            <p>Você ainda não possui consultas agendadas.</p>

        <% } else { %>

            <% for (String[] consulta : consultas) { %>

                <div class="card-consulta">

                    <div class="avatar-circle">
                        <%= escapar(consulta[2]) %>
                    </div>

                    <div class="consulta-info">
                        <span class="info-item">
                            <%= escapar(consulta[1]) %>
                        </span>

                        <span class="info-item">
                            Data: <%= escapar(consulta[3]) %>
                        </span>

                        <span class="info-item">
                            Horário: <%= escapar(consulta[4]) %>
                        </span>
                    </div>

                    <form
                        action="<%= request.getContextPath() %>/cancelar-consulta"
                        method="POST"
                        style="margin: 0;"
                    >
                        <input
                            type="hidden"
                            name="idConsulta"
                            value="<%= escapar(consulta[0]) %>"
                        >

                        <button
                            type="submit"
                            class="btn-cancelar"
                            onclick="return confirm('Tem certeza que deseja cancelar esta consulta?')"
                        >
                            Cancelar
                        </button>
                    </form>

                </div>

            <% } %>

        <% } %>

    </div>
</main>

</body>
</html>