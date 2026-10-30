package br.com.cannamed.controller;
import br.com.cannamed.util.*;import br.com.cannamed.service.*;
import jakarta.servlet.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
import java.io.*;import java.net.URLEncoder;import java.nio.charset.StandardCharsets;import java.sql.*;import java.time.*;
@WebServlet(urlPatterns={"/pacientes","/cadastrar-paciente"},loadOnStartup=1)
public class  PacientesServlet extends HttpServlet {
 @Override public void init() throws ServletException {try{InicializadorBanco.inicializar();}catch(Exception e){throw new ServletException(e);}}
 private String p(HttpServletRequest r,String k){String v=r.getParameter(k);return v==null?"":v.trim();}
 private void redirect(HttpServletResponse r,String page,String msg)throws IOException{r.sendRedirect(page+"?msg="+URLEncoder.encode(msg,StandardCharsets.UTF_8));}
 private boolean admin(HttpServletRequest r){return r.getSession(false)!=null && Boolean.TRUE.equals(r.getSession(false).getAttribute("admin"));}
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException {
  if(!admin(req)){res.sendError(403,"Área administrativa: entre com uma conta administradora.");return;}
  req.setAttribute("records",records());req.getRequestDispatcher("/pacientes.jsp").forward(req,res);
 }
 private java.util.List<String[]> records() throws ServletException {
  java.util.List<String[]> out=new java.util.ArrayList<>();
  String q="SELECT p.id_paciente,u.nome,u.email,p.cpf,p.data_nascimento FROM pacientes p JOIN usuarios u ON u.id_usuario=p.id_usuario ORDER BY p.id_paciente DESC";
  try(Connection c=Conexao.conectar();PreparedStatement s=c.prepareStatement(q);ResultSet rs=s.executeQuery()){
   while(rs.next())out.add(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)});
  }catch(SQLException e){throw new ServletException(e);}return out;
 }
 @Override protected void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException {
  req.setCharacterEncoding("UTF-8");String action=p(req,"acao");
  if(req.getServletPath().equals("/cadastrar-paciente"))action="criar";
  try {
   if("criar".equals(action)){
    if(!p(req,"senha").equals(p(req,"confirmarSenha")))throw new IllegalArgumentException("Senhas diferentes");
    new CadastroPacienteService().cadastrar(p(req,"nome"),p(req,"email"),p(req,"senha"),p(req,"cpf").replaceAll("\\D",""),LocalDate.parse(p(req,"dataNascimento")));
    redirect(res,"entrar.html","Cadastro realizado! Entre com sua conta.");return;
   }
   if(!admin(req)){res.sendError(403,"Somente administradores podem alterar pacientes");return;}
   int id=Integer.parseInt(p(req,"id"));
   try(Connection c=Conexao.conectar()){
    c.setAutoCommit(false);
    try {
     if("editar".equals(action)){
      String nome=p(req,"nome"),email=p(req,"email"),cpf=p(req,"cpf").replaceAll("\\D","");LocalDate nasc=LocalDate.parse(p(req,"dataNascimento"));
      if(nome.isBlank()||!email.contains("@")||!cpf.matches("\\d{11}")||nasc.isAfter(LocalDate.now()))throw new IllegalArgumentException("Dados inválidos");
      try(PreparedStatement s=c.prepareStatement("UPDATE usuarios SET nome=?,email=? WHERE id_usuario=(SELECT id_usuario FROM pacientes WHERE id_paciente=?)")){s.setString(1,nome);s.setString(2,email);s.setInt(3,id);if(s.executeUpdate()!=1)throw new SQLException("Paciente não encontrado");}
      try(PreparedStatement s=c.prepareStatement("UPDATE pacientes SET cpf=?,data_nascimento=? WHERE id_paciente=?")){s.setString(1,cpf);s.setDate(2,Date.valueOf(nasc));s.setInt(3,id);s.executeUpdate();}
     }else if("excluir".equals(action)){
      int uid;
      try(PreparedStatement s=c.prepareStatement("SELECT id_usuario FROM pacientes WHERE id_paciente=?")){s.setInt(1,id);try(ResultSet rs=s.executeQuery()){if(!rs.next())throw new SQLException("Paciente não encontrado");uid=rs.getInt(1);}}
      try(PreparedStatement s=c.prepareStatement("SELECT COUNT(*) FROM consultas WHERE id_paciente=?")){s.setInt(1,id);try(ResultSet rs=s.executeQuery()){rs.next();if(rs.getInt(1)>0)throw new IllegalArgumentException("Paciente possui consultas. Cancele/remova os vínculos antes de excluir.");}}
      try(PreparedStatement s=c.prepareStatement("DELETE FROM pacientes WHERE id_paciente=?")){s.setInt(1,id);s.executeUpdate();}
      try(PreparedStatement s=c.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?")){s.setInt(1,uid);s.executeUpdate();}
     }else throw new IllegalArgumentException("Operação inválida");
     c.commit();
    }catch(Exception e){c.rollback();throw e;}
   }
   redirect(res,"pacientes","Operação concluída");
  }catch(Exception e){redirect(res,"erro.html",e.getMessage()==null?"Erro no cadastro":e.getMessage());}
 }
}
