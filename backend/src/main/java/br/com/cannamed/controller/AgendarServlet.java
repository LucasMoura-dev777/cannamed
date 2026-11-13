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
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebServlet("/agendar")
public class AgendarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        Integer idUsuario = (Integer) session.getAttribute("uid");

        if (idUsuario == null) {
            idUsuario = 1;
            session.setAttribute("uid", idUsuario);
        }

        String dataStr = request.getParameter("dataEscolhida");
        String horarioStr = request.getParameter("horarioEscolhido");
        String medicoKey = request.getParameter("medicoEscolhido");

        if (medicoKey == null) medicoKey = "thiago";

        try (Connection conexao = Conexao.conectar()) {

            int idPaciente = -1;
            try (PreparedStatement ps = conexao.prepareStatement("SELECT id_paciente FROM pacientes WHERE id_usuario = ?")) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idPaciente = rs.getInt("id_paciente");
                    }
                }
            }

            if (idPaciente == -1) {

                try (PreparedStatement psIns = conexao.prepareStatement("INSERT INTO pacientes (id_usuario, data_nascimento, cpf) VALUES (?, '2000-01-01', '12345678901')", Statement.RETURN_GENERATED_KEYS)) {
                    psIns.setInt(1, idUsuario);
                    psIns.executeUpdate();
                    try (ResultSet rsKeys = psIns.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idPaciente = rsKeys.getInt(1);
                        }
                    }
                }
            }


            int idProfissional = obterOuCriarProfissional(conexao, medicoKey);


            LocalDate dataSql = LocalDate.of(2026, Integer.parseInt(dataStr.split("/")[1]), Integer.parseInt(dataStr.split("/")[0]));
            LocalTime horaInicio = LocalTime.parse(horarioStr + ":00");
            LocalTime horaFim = horaInicio.plusHours(1);

            int idDisponibilidade = -1;
            String sqlDisp = "INSERT INTO disponibilidades (id_profissional, data, hora_inicio, hora_fim) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtDisp = conexao.prepareStatement(sqlDisp, Statement.RETURN_GENERATED_KEYS)) {
                stmtDisp.setInt(1, idProfissional);
                stmtDisp.setObject(2, dataSql);
                stmtDisp.setObject(3, horaInicio);
                stmtDisp.setObject(4, horaFim);
                stmtDisp.executeUpdate();
                try (ResultSet rsDisp = stmtDisp.getGeneratedKeys()) {
                    if (rsDisp.next()) {
                        idDisponibilidade = rsDisp.getInt(1);
                    }
                }
            }


            String sqlConsulta = "INSERT INTO consultas (id_paciente, id_profissional, id_disponibilidade, data_agendamento, status, observacao) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtCons = conexao.prepareStatement(sqlConsulta)) {
                stmtCons.setInt(1, idPaciente);
                stmtCons.setInt(2, idProfissional);
                stmtCons.setInt(3, idDisponibilidade);
                stmtCons.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmtCons.setString(5, "AGENDADA");
                stmtCons.setString(6, "Consulta agendada via portal");
                stmtCons.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("erro.jsp?msg=" + URLEncoder.encode("Erro ao efetuar o agendamento: " + e.getMessage(), StandardCharsets.UTF_8));
            return;
        }

        response.sendRedirect("minhas-consultas.jsp");
    }

    private int obterOuCriarProfissional(Connection conexao, String medicoKey) throws SQLException {

        String nomeMedico = "Dr. Thiago Felipe";
        String reg = "CRM123";
        if (medicoKey.equals("daniel")) { nomeMedico = "Dr. Daniel Oliveira"; reg = "CRM124"; }
        else if (medicoKey.equals("matheus")) { nomeMedico = "Dr. Matheus Medeiros"; reg = "CRM125"; }
        else if (medicoKey.equals("lucas")) { nomeMedico = "Dr. Lucas Moura"; reg = "CRM126"; }

        try (PreparedStatement ps = conexao.prepareStatement("SELECT id_profissional FROM profissionais WHERE registro_profissional = ?")) {
            ps.setString(1, reg);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_profissional");
            }
        }


        int idEspec = 1;
        try (Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery("SELECT id_especialidade FROM especialidades LIMIT 1")) {
            if (rs.next()) {
                idEspec = rs.getInt("id_especialidade");
            } else {
                try (PreparedStatement psEsp = conexao.prepareStatement("INSERT INTO especialidades (nome, descricao) VALUES ('Cannabis Medicinal', 'Tratamentos com canabinoides')", Statement.RETURN_GENERATED_KEYS)) {
                    psEsp.executeUpdate();
                    try (ResultSet rsE = psEsp.getGeneratedKeys()) { if (rsE.next()) idEspec = rsE.getInt(1); }
                }
            }
        }

        int idUserMed = -1;
        try (PreparedStatement psUsr = conexao.prepareStatement("INSERT INTO usuarios (nome, email, senha, tipo_usuario, status) VALUES (?, ?, '123', 'MEDICO', 'ATIVO')", Statement.RETURN_GENERATED_KEYS)) {
            psUsr.setString(1, nomeMedico);
            psUsr.setString(2, medicoKey + "@cannamed.com");
            psUsr.executeUpdate();
            try (ResultSet rsU = psUsr.getGeneratedKeys()) { if (rsU.next()) idUserMed = rsU.getInt(1); }
        }

        int idProf = -1;
        try (PreparedStatement psProf = conexao.prepareStatement("INSERT INTO profissionais (id_usuario, registro_profissional, descricao, id_especialidade) VALUES (?, ?, 'Médico especialista', ?)", Statement.RETURN_GENERATED_KEYS)) {
            psProf.setInt(1, idUserMed);
            psProf.setString(2, reg);
            psProf.setInt(3, idEspec);
            psProf.executeUpdate();
            try (ResultSet rsP = psProf.getGeneratedKeys()) { if (rsP.next()) idProf = rsP.getInt(1); }
        }

        return idProf;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String medico = request.getParameter("medico");
        response.sendRedirect("agendar.jsp" + (medico != null ? "?medico=" + medico : ""));
    }
}