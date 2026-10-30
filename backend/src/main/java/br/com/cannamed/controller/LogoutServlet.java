package br.com.cannamed.controller;
import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;
@WebServlet("/sair")public class LogoutServlet extends HttpServlet {protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException {if(r.getSession(false)!=null)r.getSession(false).invalidate();s.sendRedirect("index.html");}}
