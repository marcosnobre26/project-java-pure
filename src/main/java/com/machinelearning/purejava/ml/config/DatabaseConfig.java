package com.machinelearning.purejava.ml.config; // Certifique-se que esta linha está correta!

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Classe de configuração para detalhes de conexão com o banco de dados.
 * Inclui driver JDBC, URL, usuário, senha e query/tabela.
 */
public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());

    @JsonProperty("jdbcDriver")
    private String jdbcDriver; // Ex: "org.postgresql.Driver", "com.mysql.cj.jdbc.Driver", "org.h2.Driver"

    @JsonProperty("jdbcUrl")
    private String jdbcUrl; // URL de conexão (ex: "jdbc:postgresql://localhost:5432/mydatabase")

    @JsonProperty("username")
    private String username; // Nome de usuário do banco de dados

    @JsonProperty("password")
    private String password; // Senha do banco de dados

    @JsonProperty("tableName")
    private String tableName; // Nome da tabela a ser consultada (opcional, se usar query)

    @JsonProperty("query")
    private String query; // Query SQL completa para buscar os dados (opcional, alternativa à tableName)

    // Nota: É comum usar um Properties object ou Map para configurações adicionais de JDBC
    // @JsonProperty("connectionProperties")
    // private Map<String, String> connectionProperties;


    // --- Getters e Setters para as Propriedades ---

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    // public Map<String, String> getConnectionProperties() { return connectionProperties; }
    // public void setConnectionProperties(Map<String, String> connectionProperties) { this.connectionProperties = connectionProperties; }


    /**
     * Carrega a configuração do banco de dados a partir de um arquivo YAML.
     * @param configFilePath O caminho para o arquivo YAML.
     * @return Uma instância de DatabaseConfig populada.
     * @throws IOException Se houver um erro ao ler o arquivo.
     */
    public static DatabaseConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.severe("Arquivo de configuração do banco de dados não encontrado: " + configFilePath);
            throw new IOException("Arquivo de configuração do banco de dados não encontrado: " + configFilePath);
        }
        DatabaseConfig config = mapper.readValue(configFile, DatabaseConfig.class);
        LOGGER.info("Configuração do banco de dados carregada com sucesso de: " + configFilePath);
        return config;
    }
}