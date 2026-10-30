package br.com.cannamed.controller;
import br.com.cannamed.util.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;import java.io.*;import java.sql.*;import java.net.*;import java.nio.charset.StandardCharsets;import org.mindrot.jbcrypt.BCrypt;
@WebServlet("/login-paciente")
public class LoginServlet extends HttpServlet {
 @Override protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException {
  r.setCharacterEncoding("UTF-8");String email=r.getParameter("email"),senha=r.getParameter("senha");
  if(email==null||senha==null){s.sendRedirect("entrar.html?erro=1");return;}
  try(Connection c=Conexao.conectar();PreparedStatement ps=c.prepareStatement("SELECT id_usuario,nome,senha,tipo_usuario FROM usuarios WHERE email=? AND status='ATIVO'")){
   ps.setString(1,email.trim());try(ResultSet rs=ps.executeQuery()){
    if(rs.next() && rs.getString("senha").startsWith("$2") && BCrypt.checkpw(senha,rs.getString("senha"))){
     r.getSession().invalidate();HttpSession session=r.getSession(true);r.changeSessionId();session.setAttribute("uid",rs.getInt("id_usuario"));session.setAttribute("nome",rs.getString("nome"));session.setAttribute("admin","ADMIN".equalsIgnoreCase(rs.getString("tipo_usuario")));s.sendRedirect("index.html");return;
    }
   }
  }catch(Exception e){getServletContext().log("Falha no login",e);}
  s.sendRedirect("entrar.html?erro=1");
 }
}
