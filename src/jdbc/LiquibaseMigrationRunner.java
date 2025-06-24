package jdbc;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.Scope;
import liquibase.resource.ResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseMigrationRunner {

    public static void main(String[] args) {
        final String url = "jdbc:mysql://localhost/storegame";
        final String usuario = "root";
        final String senha = "";

        final String changelogFile = "db/changelog/db.changelog-master.xml";

        Connection connection = null;
        Database database = null;
        Liquibase liquibase = null;

        try {
            connection = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão JDBC estabelecida com sucesso.");

            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            ResourceAccessor resourceAccessor = new liquibase.resource.ClassLoaderResourceAccessor(); // Ou esta, se não tiver certeza
            liquibase = new Liquibase(changelogFile, resourceAccessor, database);

            System.out.println("Iniciando migrações com Liquibase...");
            liquibase.update(new Contexts(), new LabelExpression());

            System.out.println("Migrações do Liquibase executadas com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro de conexão SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (LiquibaseException e) {
            System.err.println("Erro durante a execução do Liquibase: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (database != null) {
                try {
                    database.close();
                } catch (LiquibaseException e) {
                    System.err.println("Erro ao fechar o banco de dados do Liquibase: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão SQL: " + e.getMessage());
                }
            }
        }
    }
}
