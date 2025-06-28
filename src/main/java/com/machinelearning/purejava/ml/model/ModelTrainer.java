package com.machinelearning.purejava.ml.model;

import com.machinelearning.purejava.ml.config.TrainingConfig;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.AbstractClassifier; // IMPORTANTE: Adicione este import
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.File;
import java.util.logging.Logger;
import java.io.IOException;

public class ModelTrainer {

    private static final Logger LOGGER = Logger.getLogger(ModelTrainer.class.getName());
    private TrainingConfig trainingConfig;

    public ModelTrainer(TrainingConfig trainingConfig) {
        this.trainingConfig = trainingConfig;
        LOGGER.info("ModelTrainer inicializado com TrainingConfig: " + trainingConfig);
    }

    public Classifier trainModel(Instances data) throws Exception {
        Classifier classifier; // Aqui é a interface
        String modelType = trainingConfig.getModelType();

        switch (modelType.toUpperCase()) {
            case "J48":
                classifier = new J48();
                break;
            default:
                throw new IllegalArgumentException("Tipo de modelo desconhecido: " + modelType);
        }

        // Aplicar opções do modelo
        String[] options = trainingConfig.getModelOptions().toArray(new String[0]);
        // FAZER O CAST AQUI para AbstractClassifier para acessar setOptions()
        if (classifier instanceof AbstractClassifier) {
            ((AbstractClassifier) classifier).setOptions(options); // Correção aqui
            LOGGER.info("Opções do modelo " + modelType + " definidas como: " + String.join(" ", options));
        } else {
            LOGGER.warning("Classificador " + modelType + " não suporta setOptions diretamente. Ignorando opções.");
        }


        // Treinar o classificador
        LOGGER.info("Construindo o classificador " + modelType + "...");
        classifier.buildClassifier(data);
        LOGGER.info("Classificador " + modelType + " construído com sucesso.");
        return classifier;
    }

    public void saveModel(Classifier model) throws Exception {
        String outputPath = trainingConfig.getModelOutputPath();
        File outputFile = new File(outputPath);

        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
            LOGGER.info("Diretórios criados para salvar o modelo: " + parentDir.getAbsolutePath());
        }

        SerializationHelper.write(outputPath, model);
        LOGGER.info("Modelo salvo com sucesso em: " + outputPath);
    }

    public static Classifier loadModel(String modelPath) throws Exception {
        Classifier model = (Classifier) SerializationHelper.read(modelPath);
        LOGGER.info("Modelo carregado de: " + modelPath);
        return model;
    }
}