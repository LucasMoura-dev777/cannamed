package br.com.cannamed.controller;
import jakarta.servlet.*;import jakarta.servlet.annotation.WebListener;import br.com.cannamed.util.*;import org.mindrot.jbcrypt.BCrypt;import java.sql.*;
@WebListener public class AdminBootstrap implements ServletContextListener {
 public void contextInitialized(ServletContextEvent e){InicializadorBanco.inicializar();String email=System.getenv("CANNAMED_ADMIN_EMAIL"),password=System.getenv("CANNAMED_ADMIN_PASSWORD");if(email==null||password==null||password.length()<8)return;
 try(Connection c=Conexao.conectar();PreparedStatement s=c.prepareStatement("INSERT INTO usuarios(nome,email,senha,tipo_usuario,status) SELECT 'Administrador',?,?,'ADMIN','ATIVO' WHERE NOT EXISTS(SELECT 1 FROM usuarios WHERE email=?)")){
 s.setString(1,email);s.setString(2,BCrypt.hashpw(password,BCrypt.gensalt()));s.setString(3,email);s.executeUpdate();}catch(SQLException ex){throw new IllegalStateException(ex);}}
}
