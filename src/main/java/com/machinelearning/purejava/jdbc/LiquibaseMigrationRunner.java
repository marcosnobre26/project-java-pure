package com.machinelearning.purejava.jdbc;

import com.machinelearning.purejava.ml.config.DatabaseConfig; // Importe a classe
import com.machinelearning.purejava.ml.util.MLUtils;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class LiquibaseMigrationRunner {

    private static final Logger LOGGER = Logger.getLogger(LiquibaseMigrationRunner.class.getName());

    // Não precisa de construtor se o método runMigrations for estático ou receber o config
    public static void runMigrations(DatabaseConfig dbConfig) { // Recebe DatabaseConfig
        Connection connection = null;
        Liquibase liquibase = null;
        try {
            Class.forName(dbConfig.getJdbcDriver());
            Properties props = new Properties();
            props.setProperty("user", dbConfig.getUsername());
            props.setProperty("password", dbConfig.getPassword());
            connection = DriverManager.getConnection(dbConfig.getJdbcUrl(), props);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            // Supondo que seu arquivo changelog esteja em src/main/resources
            liquibase = new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(""); // Aplica todas as migrações pendentes

            LOGGER.info("Migrações do Liquibase aplicadas com sucesso.");

        } catch (Exception e) {
            LOGGER.severe("Erro ao executar migrações do Liquibase: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Reusando seu utilitário MLUtils para fechar a conexão
            MLUtils.closeJdbcResources(null, null, connection);
        }
    }
}