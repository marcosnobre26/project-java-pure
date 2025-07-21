// Crie esta nova classe em: src/main/java/com/machinelearning/purejava/main/ModelTrainerApp.java
package com.machinelearning.purejava.main;

import com.machinelearning.purejava.ml.config.DataConfig;
import com.machinelearning.purejava.ml.config.FeatureConfig;
import com.machinelearning.purejava.ml.config.TrainingConfig;
import com.machinelearning.purejava.ml.data.DataLoader;
import com.machinelearning.purejava.ml.features.FeatureExtractor;
import com.machinelearning.purejava.ml.model.ModelTrainer;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.FileOutputStream;
import java.util.logging.Logger;

/**
 * Esta é uma aplicação separada com a única responsabilidade de treinar
 * e salvar o modelo e o extrator de features.
 * Execute esta classe uma vez para gerar os artefatos necessários para a API.
 */
public class ModelTrainerApp {

    private static final Logger LOGGER = Logger.getLogger(ModelTrainerApp.class.getName());

    public static void main(String[] args) throws Exception {
        LOGGER.info("--- INICIANDO PROCESSO DE TREINAMENTO ---");

        // 1. Carregar Configurações
        DataConfig dataConfig = DataConfig.load("src/main/resources/config/data_config.yml");
        FeatureConfig featureConfig = FeatureConfig.load("src/main/resources/config/feature_config.yml");
        TrainingConfig trainingConfig = TrainingConfig.load("src/main/resources/config/training_config.yml");

        // 2. Carregar Dados (use o dataset completo!)
        dataConfig.getCsvConfig().setFilePath("src/main/resources/data/adult_full_train.csv");
        DataLoader dataLoader = new DataLoader(dataConfig);
        Instances rawData = dataLoader.loadData();
        rawData.setClassIndex(rawData.attribute(dataConfig.getTargetColumn()).index());
        LOGGER.info("Dados completos carregados: " + rawData.numInstances() + " instâncias.");

        // 3. Pré-processar e "Treinar" o Extrator
        FeatureExtractor featureExtractor = new FeatureExtractor(featureConfig);
        Instances processedData = featureExtractor.preprocess(rawData);
        LOGGER.info("Dados pré-processados. O FeatureExtractor está treinado.");

        // 4. Treinar o Modelo
        ModelTrainer modelTrainer = new ModelTrainer(trainingConfig);
        Classifier model = modelTrainer.trainModel(processedData);
        LOGGER.info("Modelo treinado com sucesso.");

        // 5. SALVAR AMBOS OS ARTEFATOS na pasta resources/model
        String modelOutputPath = "src/main/resources/model/weka-j48-model.ser";
        String extractorOutputPath = "src/main/resources/model/feature-extractor.ser";

        SerializationHelper.write(modelOutputPath, model);
        LOGGER.info("Modelo salvo em: " + modelOutputPath);

        // Usando FileOutputStream para salvar o extrator
        try (FileOutputStream fos = new FileOutputStream(extractorOutputPath)) {
            SerializationHelper.write(fos, featureExtractor);
        }
        LOGGER.info("FeatureExtractor salvo em: " + extractorOutputPath);

        LOGGER.info("--- PROCESSO DE TREINAMENTO CONCLUÍDO ---");
    }
}