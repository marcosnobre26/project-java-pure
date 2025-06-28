package com.machinelearning.purejava.ml.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List; // Importar List para as coleções
import java.util.logging.Logger;

public class FeatureConfig {
    private static final Logger LOGGER = Logger.getLogger(FeatureConfig.class.getName());

    // --- CAMPOS DE CONFIGURAÇÃO (correspondem às chaves no feature_config.yml) ---
    @JsonProperty("replaceMissingValues")
    private boolean replaceMissingValues;

    @JsonProperty("standardizeNumeric")
    private boolean standardizeNumeric;

    @JsonProperty("oneHotEncodeCategorical")
    private boolean oneHotEncodeCategorical;

    @JsonProperty("categoricalEncoding") // Será uma String simples (ex: "ONE_HOT_ENCODING")
    private String categoricalEncoding;

    @JsonProperty("numericalFeatures")
    private List<String> numericalFeatures;

    @JsonProperty("categoricalFeatures")
    private List<String> categoricalFeatures;

    // --- CONSTRUTOR PADRÃO (NECESSÁRIO PARA JACKSON) ---
    public FeatureConfig() {
        // Inicializa listas para evitar NullPointerException se não estiverem no YAML
        this.numericalFeatures = new java.util.ArrayList<>();
        this.categoricalFeatures = new java.util.ArrayList<>();
    }

    // --- GETTERS (MÉTODOS PARA ACESSAR OS VALORES DOS CAMPOS) ---
    public boolean isReplaceMissingValues() {
        return replaceMissingValues;
    }

    public boolean isStandardizeNumeric() {
        return standardizeNumeric;
    }

    public boolean isOneHotEncodeCategorical() {
        return oneHotEncodeCategorical;
    }

    public String getCategoricalEncoding() {
        return categoricalEncoding;
    }

    public List<String> getNumericalFeatures() {
        return numericalFeatures;
    }

    public List<String> getCategoricalFeatures() {
        return categoricalFeatures;
    }

    // --- SETTERS (MÉTODOS PARA JACKSON INJETAR OS VALORES DO YAML) ---
    // Estes setters são importantes para o Jackson deserializar o YAML para o objeto Java
    public void setReplaceMissingValues(boolean replaceMissingValues) {
        this.replaceMissingValues = replaceMissingValues;
    }

    public void setStandardizeNumeric(boolean standardizeNumeric) {
        this.standardizeNumeric = standardizeNumeric;
    }

    public void setOneHotEncodeCategorical(boolean oneHotEncodeCategorical) {
        this.oneHotEncodeCategorical = oneHotEncodeCategorical;
    }

    public void setCategoricalEncoding(String categoricalEncoding) {
        this.categoricalEncoding = categoricalEncoding;
    }

    public void setNumericalFeatures(List<String> numericalFeatures) {
        this.numericalFeatures = numericalFeatures;
    }

    public void setCategoricalFeatures(List<String> categoricalFeatures) {
        this.categoricalFeatures = categoricalFeatures;
    }

    // --- MÉTODO DE CARREGAMENTO (JÁ DEVE ESTAR FUNCIONANDO COM CAMINHOS ABSOLUTOS) ---
    public static FeatureConfig load(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            LOGGER.severe("Arquivo de configuração de features não encontrado: " + configFilePath);
            throw new IOException("Arquivo de configuração de features não encontrado: " + configFilePath);
        }
        FeatureConfig config = mapper.readValue(configFile, FeatureConfig.class);
        LOGGER.info("Configuração de features carregada com sucesso de: " + configFilePath);
        return config;
    }
}