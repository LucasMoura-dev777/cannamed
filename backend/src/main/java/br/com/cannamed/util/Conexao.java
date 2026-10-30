package br.com.cannamed.util;
import java.sql.*;
public class Conexao {
    private static final String URL = "jdbc:h2:./data/cannamed";
    public static Connection conectar() throws SQLException { return DriverManager.getConnection(URL,"sa",""); }
}
