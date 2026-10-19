package br.com.cannamed;

import br.com.cannamed.util.Conexao;

import java.sql.Connection;
import java.sql.ResultSet;

public class TesteBanco {

    public static void main(String[] args) {

        try (Connection conexao = Conexao.conectar()) {

            ResultSet tabelas = conexao
                    .getMetaData()
                    .getTables(null, "PUBLIC", null, new String[]{"TABLE"});

            System.out.println("=== TABELAS DO CANNAMED ===");

            while (tabelas.next()) {
                System.out.println(tabelas.getString("TABLE_NAME"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao consultar o banco.");
            e.printStackTrace();
        }
    }
}