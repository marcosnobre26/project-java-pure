package jdbc;

import org.flywaydb.core.Flyway;

import java.sql.DriverManager;

public class TesteMigration {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC do MySQL carregado com sucesso pelo Class.forName()");

            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:mysql://localhost/storegame", "root", "")
                    .driver("com.mysql.cj.jdbc.Driver")
                    .locations("filesystem:sql/migrations")
                    .load();

            flyway.migrate();

            System.out.println("Migrations executadas com sucesso :)");

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a migração:");
            e.printStackTrace();
        }
    }
}