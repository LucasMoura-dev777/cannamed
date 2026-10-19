package br.com.cannamed.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:h2:./data/cannamed";
    private static final String USUARIO = "sa";
    private static final String SENHA = "";

    public static Connection conectar() throws SQLException {

        Connection conexao = DriverManager.getConnection(
                URL,
                USUARIO,
                SENHA
        );

        System.out.println(
                "Banco conectado em: " +
                        conexao.getMetaData().getURL()
        );

        return conexao;
    }
}