package jdbc.controller;

import jdbc.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoController {
    public static List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();

        try {
            Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/storegame", "root", "");

            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nome FROM produtos");

            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("id"), rs.getString("nome")));
            }

            conexao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }
}