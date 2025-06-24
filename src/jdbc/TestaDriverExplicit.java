package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestaDriverExplicit {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // força carregamento
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/storegame", "root", "");
            System.out.println("Conexão com DB ok!");
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
