package com.machinelearning.purejava.ml.service;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.util.logging.Logger;
import java.io.File; // Para manipulação de arquivos
import java.io.IOException;

// Se PredictionService usa ModelConfig
import com.machinelearning.purejava.ml.config.ModelConfig; // Certifique-se que está importado

public class PredictionService {

    private static final Logger LOGGER = Logger.getLogger(PredictionService.class.getName());
    private Classifier model;
    private Instances headerInformation; // Usado para garantir que novas instâncias tenham a mesma estrutura

    // Construtor que aceita o modelo treinado e a estrutura (header) dos dados pré-processados
    public PredictionService(Classifier model, Instances headerInformation) {
        this.model = model;
        this.headerInformation = headerInformation;
        LOGGER.info("PredictionService inicializado.");
    }

    // Método para carregar um modelo se ele não foi passado no construtor
    // Este método pode ser o que está causando o erro se chamar getOutputPath()
    public static Classifier loadModel(ModelConfig modelConfig) throws Exception { // Pode ser estático ou não, dependendo do seu design
        // A linha problemática provavelmente está aqui ou em um método similar
        String modelPath = modelConfig.getModelOutputPath(); // CORRIGIDO: de getOutputPath() para getModelOutputPath()
        File modelFile = new File(modelPath);
        if (!modelFile.exists()) {
            throw new IOException("Arquivo do modelo não encontrado: " + modelPath);
        }
        Classifier loadedModel = (Classifier) SerializationHelper.read(modelPath);
        LOGGER.info("Modelo carregado para predição de: " + modelPath);
        return loadedModel;
    }

    public String predict(Instance newSample) throws Exception {
        if (model == null) {
            throw new IllegalStateException("Modelo não foi carregado ou treinado para predição.");
        }
        if (newSample.classIndex() == -1) {
            // Se a instância de entrada não tem índice de classe, atribua o do headerInformation
            // Isso é crucial para que o Weka saiba onde o atributo alvo estaria (mesmo que vazio).
            newSample.setDataset(headerInformation);
            // newSample.setClassMissing(); // Define a classe como ausente para predição
        }

        double predictedClassIndex = model.classifyInstance(newSample);
        String predictedClassLabel = newSample.classAttribute().value((int) predictedClassIndex);

        return predictedClassLabel;
    }
}