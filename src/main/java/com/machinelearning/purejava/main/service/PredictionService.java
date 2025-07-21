// Local: src/main/java/com/machinelearning/purejava/main/service/PredictionService.java
package com.machinelearning.purejava.main.service;

import com.machinelearning.purejava.main.dto.PredictionResponse;
import com.machinelearning.purejava.ml.features.FeatureExtractor;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PredictionService {

    private static final Logger LOGGER = Logger.getLogger(PredictionService.class.getName());
    private Classifier model;
    private FeatureExtractor featureExtractor;

    @PostConstruct
    public void loadModelAndExtractor() {
        try {
            LOGGER.info("Carregando modelo Weka e FeatureExtractor...");
            String modelPath = "/model/weka-j48-model.ser";
            InputStream modelStream = PredictionService.class.getResourceAsStream(modelPath);
            if (modelStream == null) throw new IllegalStateException("Modelo não encontrado em: " + modelPath);
            model = (Classifier) SerializationHelper.read(modelStream);
            LOGGER.info("Modelo Weka carregado com sucesso.");

            String extractorPath = "/model/feature-extractor.ser";
            InputStream extractorStream = PredictionService.class.getResourceAsStream(extractorPath);
            if (extractorStream == null) throw new IllegalStateException("FeatureExtractor não encontrado em: " + extractorPath);
            featureExtractor = (FeatureExtractor) SerializationHelper.read(extractorStream);
            LOGGER.info("FeatureExtractor carregado com sucesso.");
        } catch (Exception e) {
            LOGGER.severe("Falha crítica ao carregar artefatos de ML: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Realiza a predição e retorna um objeto de resposta enriquecido.
     * @param features Um mapa contendo os dados da requisição da API.
     * @return Um objeto PredictionResponse com a classe prevista e as probabilidades.
     */
    public PredictionResponse predict(Map<String, Object> features) throws Exception {
        if (model == null || featureExtractor == null) {
            throw new IllegalStateException("Serviço de predição não foi inicializado corretamente.");
        }

        LOGGER.info("Recebido pedido de predição com os dados: " + features);

        // Passos 1 e 2: Criar e pré-processar a instância
        Instance newRawInstance = featureExtractor.createWekaInstanceFromMap(features);
        Instances preprocessedDataSet = featureExtractor.preprocessNewSample(newRawInstance);
        Instance instanceToPredict = preprocessedDataSet.firstInstance();

        // --- LÓGICA MELHORADA ---
        // Passo 3: Obter a distribuição de probabilidade
        double[] probabilityDistribution = model.distributionForInstance(instanceToPredict);

        // Passo 4: Mapear as probabilidades para os nomes das classes
        Attribute classAttribute = preprocessedDataSet.classAttribute();
        Map<String, Double> probabilitiesMap = new HashMap<>();
        for (int i = 0; i < probabilityDistribution.length; i++) {
            String className = classAttribute.value(i);
            double probability = probabilityDistribution[i];
            probabilitiesMap.put(className.trim(), probability); // .trim() para remover espaços
        }
        LOGGER.info("Probabilidades calculadas: " + probabilitiesMap);

        // Passo 5: Obter a classe com a maior probabilidade
        double predictionIndex = model.classifyInstance(instanceToPredict);
        String predictedClass = classAttribute.value((int) predictionIndex).trim();
        LOGGER.info("Classe prevista: " + predictedClass);

        // Passo 6: Montar e retornar o objeto de resposta completo
        return new PredictionResponse(predictedClass, probabilitiesMap);
    }
}