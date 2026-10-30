package br.com.cannamed.util;
import org.h2.tools.RunScript;
import java.io.*;import java.nio.charset.StandardCharsets;import java.sql.*;
public class InicializadorBanco {
 public static void inicializar() {
  try(Connection c=Conexao.conectar();InputStream in=InicializadorBanco.class.getClassLoader().getResourceAsStream("schema.sql")) {
   if(in==null) throw new IllegalStateException("schema.sql não encontrado");
   RunScript.execute(c,new InputStreamReader(in,StandardCharsets.UTF_8));
   try(Statement s=c.createStatement()) {s.execute("ALTER TABLE PUBLIC.PACIENTES ADD COLUMN IF NOT EXISTS CPF VARCHAR(11)");}
  } catch(Exception e){throw new IllegalStateException("Falha ao iniciar H2",e);}
 }
}
