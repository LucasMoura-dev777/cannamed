package br.com.cannamed.util;

import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class InicializadorBanco {

    public static void inicializar() {

        try (
                Connection conexao = Conexao.conectar();

                InputStreamReader reader = new InputStreamReader(
                        InicializadorBanco.class
                                .getClassLoader()
                                .getResourceAsStream("schema.sql"),
                        StandardCharsets.UTF_8
                )
        ) {

            RunScript.execute(conexao, reader);

            System.out.println("Tabelas criadas com sucesso!");

        } catch (Exception e) {

            System.out.println("Erro ao criar as tabelas");
            e.printStackTrace();
        }
    }
}