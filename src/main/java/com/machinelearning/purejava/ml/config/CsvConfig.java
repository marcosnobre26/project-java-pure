package com.machinelearning.purejava.ml.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.logging.Logger;

public class CsvConfig {
    private static final Logger LOGGER = Logger.getLogger(CsvConfig.class.getName());

    @JsonProperty("filePath")
    private String filePath;

    @JsonProperty("hasHeader")
    private boolean hasHeader;

    @JsonProperty("separator")
    private String separator;

    // Construtor padrão (necessário para Jackson)
    public CsvConfig() {}

    // Getters
    public String getFilePath() {
        return filePath;
    }

    public boolean hasHeader() { // Nome de getter para booleanos é comum ser is ou has
        return hasHeader;
    }

    public String getSeparator() {
        return separator;
    }

    // Setters
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return "CsvConfig{" +
               "filePath='" + filePath + '\'' +
               ", hasHeader=" + hasHeader +
               ", separator='" + separator + '\'' +
               '}';
    }
}