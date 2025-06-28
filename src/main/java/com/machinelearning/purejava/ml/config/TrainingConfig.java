package com.machinelearning.purejava.ml.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List; // Importar List para modelOptions
import java.util.logging.Logger;

public class TrainingConfig {
    private static final Logger LOGGER = Logger.getLogger(TrainingConfig.class.getName());

    // --- CAMPOS DE CONFIGURAÇÃO (correspondem às chaves no training_config.yml) ---
    @JsonProperty("modelType")
    private String modelType;

    @JsonProperty("modelOptions")
    private List<String> modelOptions; // Para opções como "-C", "0.25", "-M", "2"

    @JsonProperty("modelOutputPath")
    private String modelOutputPath;

    @JsonProperty("crossValidationFolds") // Se você usa esta propriedade (opcional)
    private int crossValidationFolds;

    // --- CONSTRUTOR PADRÃO (NECESSÁRIO PARA JACKSON) ---
    public TrainingConfig() {
        // Inicializa listas para evitar NullPointerException se não estiverem no YAML
        this.modelOptions = new java.util.ArrayList<>();
    }

    // --- GETTERS (MÉTODOS PARA ACESSAR OS VALORES DOS CAMPOS) ---
    public String getModelType() {
        return modelType;
    }

    public List<String> getModelOptions() {
        return modelOptions;
    }

    public String getModelOutputPath() {
        return modelOutputPath;
    }

    public int getCrossValidationFolds() { // Getter para crossValidationFolds
        return crossValidationFolds;
    }

    // --- SETTERS (MÉTODOS PARA JACKSON INJETAR OS VALORES DO YAML) ---
    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public void setModelOptions(List<String> modelOptions) {
        this.modelOptions = modelOptions;
    }

    public void setModelOutputPath(String modelOutputPath) {
        this.modelOutputPath = modelOutputPath;
    }

    public void setCrossValidationFolds(int crossValidationFolds) { // Setter para crossValidationFolds
        this.crossValidationFolds = crossValidationFolds;
    }


    // --- MÉTODO DE CARREGAMENTO ---
    public static TrainingConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.severe("Arquivo de configuração de treinamento não encontrado: " + configFilePath);
            throw new IOException("Arquivo de configuração de treinamento não encontrado: " + configFilePath);
        }
        TrainingConfig config = mapper.readValue(configFile, TrainingConfig.class); // Linha 33
        LOGGER.info("Configuração de treinamento carregada com sucesso de: " + configFilePath);
        return config;
    }
}