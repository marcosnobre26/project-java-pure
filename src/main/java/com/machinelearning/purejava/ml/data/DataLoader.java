package com.machinelearning.purejava.ml.data;

import com.machinelearning.purejava.ml.config.DataConfig;
import com.machinelearning.purejava.ml.config.CsvConfig;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.util.logging.Logger;
import java.io.IOException;

public class DataLoader {

    private static final Logger LOGGER = Logger.getLogger(DataLoader.class.getName());
    private DataConfig dataConfig;

    public DataLoader(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
        LOGGER.info("DataLoader inicializado com DataConfig: " + dataConfig);
    }

    public Instances loadData() throws IOException {
        Instances data = null;
        String sourceType = dataConfig.getSourceType();

        if ("CSV".equalsIgnoreCase(sourceType)) {
            CsvConfig csvConfig = dataConfig.getCsvConfig();
            if (csvConfig == null) {
                throw new IOException("CsvConfig não pode ser nulo para sourceType CSV.");
            }

            String csvFilePath = csvConfig.getFilePath();
            boolean hasHeader = csvConfig.hasHeader(); // Ainda vamos usar esta informação para lógica posterior, se necessário
            String separator = csvConfig.getSeparator();

            File inputFile = new File(csvFilePath);
            if (!inputFile.exists()) {
                throw new IOException("Arquivo CSV não encontrado no caminho: " + csvFilePath);
            }

            LOGGER.info("Carregando CSV de: " + csvFilePath);
            CSVLoader loader = new CSVLoader();
            loader.setSource(inputFile);
            loader.setFieldSeparator(separator);
            // REMOVIDA: loader.setNoHeaderOnly(!hasHeader); // Esta linha causava o erro.

            data = loader.getDataSet();
            LOGGER.info("CSV carregado com sucesso. Instâncias: " + data.numInstances() + ", Atributos: " + data.numAttributes());

        } else if ("DATABASE".equalsIgnoreCase(sourceType)) {
            LOGGER.warning("Carregamento de DATABASE não implementado.");
            throw new UnsupportedOperationException("Carregamento de dados de banco de dados ainda não implementado.");
        } else {
            throw new IllegalArgumentException("SourceType desconhecido: " + sourceType);
        }

        return data;
    }
}