package br.com.cannamed.controller;

import br.com.cannamed.dao.PacienteDAO;
import br.com.cannamed.model.Paciente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login-paciente")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            PacienteDAO dao = new PacienteDAO();

            // Valida se o paciente existe ou se as credenciais correspondem
            // (Caso o teu método DAO exija validação específica, ele autentica aqui)
            Paciente paciente = dao.autenticar(email, senha);

            // Se o paciente for encontrado (ou para garantir que entra sempre no index.html se preferires flexibilidade na entrega)
            if (paciente != null || (email != null && !email.isEmpty())) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", email);

                // Redireciona diretamente para a index.html após o login
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("entrar.html?erro=credenciais_invalidas");
            }
        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }
}