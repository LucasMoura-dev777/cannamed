package br.com.cannamed;

import br.com.cannamed.util.Conexao;
import br.com.cannamed.util.InicializadorBanco;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        try (Connection conexao = Conexao.conectar()) {

            System.out.println("Conexão com H2 realizada com sucesso!");

        } catch (Exception e) {

            System.out.println("Erro ao conectar ao banco.");
            e.printStackTrace();
            return;
        }

        InicializadorBanco.inicializar();
    }
}