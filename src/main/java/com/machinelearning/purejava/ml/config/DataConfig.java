package com.machinelearning.purejava.ml.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class DataConfig {
    private static final Logger LOGGER = Logger.getLogger(DataConfig.class.getName());

    @JsonProperty("targetColumn") // Adicionado, baseado na sua imagem
    private String targetColumn;

    @JsonProperty("sourceType") // NOVO CAMPO
    private String sourceType;

    @JsonProperty("csvConfig") // NOVO CAMPO, referenciando a nova classe CsvConfig
    private CsvConfig csvConfig;

    // Construtor padrão (necessário para Jackson)
    public DataConfig() {}

    // Getters
    public String getTargetColumn() { // Adicionado
        return targetColumn;
    }

    public String getSourceType() { // NOVO GETTER
        return sourceType;
    }

    public CsvConfig getCsvConfig() { // NOVO GETTER
        return csvConfig;
    }

    // Setters
    public void setTargetColumn(String targetColumn) { // Adicionado
        this.targetColumn = targetColumn;
    }

    public void setSourceType(String sourceType) { // NOVO SETTER
        this.sourceType = sourceType;
    }

    public void setCsvConfig(CsvConfig csvConfig) { // NOVO SETTER
        this.csvConfig = csvConfig;
    }

    // Método de carregamento
    public static DataConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.severe("Arquivo de configuração de dados não encontrado: " + configFilePath);
            throw new IOException("Arquivo de configuração de dados não encontrado: " + configFilePath);
        }
        DataConfig config = mapper.readValue(configFile, DataConfig.class);
        LOGGER.info("Configuração de dados carregada com sucesso de: " + configFilePath);
        return config;
    }

    @Override
    public String toString() {
        return "DataConfig{" +
               "targetColumn='" + targetColumn + '\'' +
               ", sourceType='" + sourceType + '\'' +
               ", csvConfig=" + csvConfig +
               '}';
    }
}