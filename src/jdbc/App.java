package jdbc;

import static spark.Spark.*;
import jdbc.controller.ProdutoController;
import jdbc.model.Produto;
import com.google.gson.Gson;

public class App {
    public static void main(String[] args) {
        port(4567);

        get("/produtos", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(ProdutoController.listarProdutos());
        });

        System.out.println("API rodando em http://localhost:4567/produtos");
    }
}