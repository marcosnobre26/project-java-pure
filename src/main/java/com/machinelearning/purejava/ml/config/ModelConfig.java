package com.machinelearning.purejava.ml.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ModelConfig {
    private static final Logger LOGGER = Logger.getLogger(ModelConfig.class.getName());

    @JsonProperty("modelOutputPath") // Esta é a propriedade que PredictionService procura
    private String modelOutputPath;

    public ModelConfig() {}

    // Getter para modelOutputPath
    public String getModelOutputPath() {
        return modelOutputPath;
    }

    // Setter para modelOutputPath
    public void setModelOutputPath(String modelOutputPath) {
        this.modelOutputPath = modelOutputPath;
    }

    public static ModelConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.warning("Arquivo de configuração de modelo não encontrado (pode ser opcional): " + configFilePath);
            return new ModelConfig();
        }
        ModelConfig config = mapper.readValue(configFile, ModelConfig.class);
        LOGGER.info("Configuração de modelo carregada com sucesso de: " + configFilePath);
        return config;
    }

    @Override
    public String toString() {
        return "ModelConfig{" +
               "modelOutputPath='" + modelOutputPath + '\'' +
               '}';
    }
}