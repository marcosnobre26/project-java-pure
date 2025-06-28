package com.machinelearning.purejava.ml.config; // Certifique-se que esta linha está correta!

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Classe principal de configuração do Projeto de Machine Learning.
 * Carrega as configurações de um arquivo YAML.
 */
public class MLConfig {

    private static final Logger LOGGER = Logger.getLogger(MLConfig.class.getName());

    // Caminho para o arquivo de configuração principal (ex: application.yml)
    // Se você tiver um único arquivo grande, pode ser o caminho para ele.
    // Ou pode ser a classe que contém outras configurações.
    @JsonProperty("applicationName")
    private String applicationName;

    @JsonProperty("logLevel")
    private String logLevel;

    @JsonProperty("dataConfigPath")
    private String dataConfigPath; // Caminho para o arquivo de configuração de dados

    @JsonProperty("featureConfigPath")
    private String featureConfigPath; // Caminho para o arquivo de configuração de features

    @JsonProperty("modelConfigPath")
    private String modelConfigPath; // Caminho para o arquivo de configuração de modelo

    @JsonProperty("trainingConfigPath")
    private String trainingConfigPath; // Caminho para o arquivo de configuração de treinamento

    @JsonProperty("databaseConfigPath")
    private String databaseConfigPath; // Caminho para o arquivo de configuração de banco de dados


    // Getters para acessar as propriedades
    public String getApplicationName() {
        return applicationName;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getDataConfigPath() {
        return dataConfigPath;
    }

    public String getFeatureConfigPath() {
        return featureConfigPath;
    }

    public String getModelConfigPath() {
        return modelConfigPath;
    }

    public String getTrainingConfigPath() {
        return trainingConfigPath;
    }

    public String getDatabaseConfigPath() {
        return databaseConfigPath;
    }

    // Setters (Jackson pode usá-los, mas se você só carrega, não são estritamente necessários publicamente)
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setDataConfigPath(String dataConfigPath) {
        this.dataConfigPath = dataConfigPath;
    }

    public void setFeatureConfigPath(String featureConfigPath) {
        this.featureConfigPath = featureConfigPath;
    }

    public void setModelConfigPath(String modelConfigPath) {
        this.modelConfigPath = modelConfigPath;
    }

    public void setTrainingConfigPath(String trainingConfigPath) {
        this.trainingConfigPath = trainingConfigPath;
    }
    public void setDatabaseConfigPath(String databaseConfigPath) {
        this.databaseConfigPath = databaseConfigPath;
    }

    /**
     * Carrega a configuração principal a partir de um arquivo YAML.
     * @param configFilePath O caminho para o arquivo YAML.
     * @return Uma instância de MLConfig populada.
     * @throws IOException Se houver um erro ao ler o arquivo.
     */
    public static MLConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.severe("Arquivo de configuração não encontrado: " + configFilePath);
            throw new IOException("Arquivo de configuração não encontrado: " + configFilePath);
        }
        MLConfig config = mapper.readValue(configFile, MLConfig.class);
        LOGGER.info("Configuração principal carregada com sucesso de: " + configFilePath);
        return config;
    }
}