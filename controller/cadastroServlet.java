package br.com.cannamed.controller;

import br.com.cannamed.dao.PacienteDAO;
import br.com.cannamed.model.Paciente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cadastrar-paciente")
public class CadastroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");


        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String cpf = request.getParameter("cpf");
        String dataNascimento = request.getParameter("dataNascimento");


        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setSenha(senha);
        paciente.setCpf(cpf);

        try {

            PacienteDAO pacienteDAO = new PacienteDAO();
            pacienteDAO.inserir(paciente);


            response.sendRedirect("entrar.html?sucesso=cadastro");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cadastro.html?erro=falha");
        }
    }
}