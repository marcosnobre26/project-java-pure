package com.machinelearning.purejava.main;

import com.machinelearning.purejava.ml.config.DataConfig;
import com.machinelearning.purejava.ml.config.FeatureConfig;
import com.machinelearning.purejava.ml.config.TrainingConfig;
import com.machinelearning.purejava.ml.config.ModelConfig;
import com.machinelearning.purejava.ml.data.DataLoader;
import com.machinelearning.purejava.ml.features.FeatureExtractor;
import com.machinelearning.purejava.ml.model.ModelTrainer;
// import com.machinelearning.purejava.ml.service.PredictionService; // Mantenha esta linha comentada se você não corrigiu PredictionService ou se não vai usá-lo agora

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;

import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.Classifier;

public class MLApplication {

    private static final Logger LOGGER = Logger.getLogger(MLApplication.class.getName());

    public static void main(String[] args) {
        // --- INÍCIO DA CONFIGURAÇÃO DE LOG (Garante que INFO logs apareçam no console) ---
        LogManager.getLogManager().reset();
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        if (!hasConsoleHandler(rootLogger)) {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.INFO);
            rootLogger.addHandler(ch);
        }
        rootLogger.setLevel(Level.INFO);
        LOGGER.setLevel(Level.INFO);
        // --- FIM DA CONFIGURAÇÃO DE LOG ---

        LOGGER.info("Aplicação de Machine Learning Pura Java iniciada.");

        try {
            // 1. Carregar configurações
            LOGGER.info("Carregando DataConfig...");
            DataConfig dataConfig = DataConfig.load("src/main/resources/config/data_config.yml");
            LOGGER.info("DataConfig carregado: " + dataConfig);

            LOGGER.info("Carregando FeatureConfig...");
            FeatureConfig featureConfig = FeatureConfig.load("src/main/resources/config/feature_config.yml");
            LOGGER.info("FeatureConfig carregado: " + featureConfig);

            LOGGER.info("Carregando TrainingConfig...");
            TrainingConfig trainingConfig = TrainingConfig.load("src/main/resources/config/training_config.yml");
            LOGGER.info("TrainingConfig carregado: " + trainingConfig);

            LOGGER.info("Carregando ModelConfig...");
            ModelConfig modelConfig = ModelConfig.load("src/main/resources/config/model_config.yml");
            LOGGER.info("ModelConfig carregado: " + modelConfig);


            // 2. Carregar e pré-processar dados de treinamento
            LOGGER.info("Inicializando DataLoader...");
            DataLoader dataLoader = new DataLoader(dataConfig);
            LOGGER.info("Carregando dados brutos de treinamento...");
            Instances rawTrainingData = dataLoader.loadData();
            int classIndex = rawTrainingData.attribute(dataConfig.getTargetColumn()).index();
            if (classIndex != -1) {
                rawTrainingData.setClassIndex(classIndex);
                LOGGER.info("Atributo alvo '" + dataConfig.getTargetColumn() + "' definido no índice: " + classIndex);
            } else {
                LOGGER.warning("Atributo alvo '" + dataConfig.getTargetColumn() + "' não encontrado nos dados. Verifique data_config.yml e o CSV.");
            }
            LOGGER.info("Dados brutos de treinamento carregados: " + rawTrainingData.numInstances() + " instâncias, " + rawTrainingData.numAttributes() + " atributos.");


            LOGGER.info("Inicializando FeatureExtractor...");
            FeatureExtractor featureExtractor = new FeatureExtractor(featureConfig);
            LOGGER.info("Pré-processando dados de treinamento...");
            Instances preprocessedTrainingData = featureExtractor.preprocess(rawTrainingData);
            LOGGER.info("Dados de treinamento pré-processados: " + preprocessedTrainingData.numInstances() + " instâncias, " + preprocessedTrainingData.numAttributes() + " atributos.");


            // 3. Treinar e Salvar o Modelo
            LOGGER.info("Inicializando ModelTrainer...");
            ModelTrainer modelTrainer = new ModelTrainer(trainingConfig);
            LOGGER.info("Iniciando treinamento do modelo com tipo: " + trainingConfig.getModelType() + " e opções: " + trainingConfig.getModelOptions());
            Classifier model = modelTrainer.trainModel(preprocessedTrainingData);
            LOGGER.info("Modelo treinado com sucesso.");

            modelTrainer.saveModel(model);
            LOGGER.info("Modelo salvo em: " + trainingConfig.getModelOutputPath());


            // --- Exemplo de como você faria uma predição com uma nova amostra ---
            // MANTENHA ESTE BLOCO COMENTADO POR ENQUANTO para resolver os erros de compilação
            // do PredictionService, ou certifique-se de que PredictionService.java
            // foi corrigido para usar .getModelOutputPath()
            /*
            LOGGER.info("Preparando nova amostra para predição...");
            Map<String, Object> newSampleData = new HashMap<>();
            newSampleData.put("age", 25);
            newSampleData.put("workclass", "Private");
            newSampleData.put("fnlwgt", 226802);
            newSampleData.put("education", "11th");
            newSampleData.put("education_num", 7);
            newSampleData.put("marital_status", "Never-married");
            newSampleData.put("occupation", "Machine-op-inspct");
            newSampleData.put("relationship", "Own-child");
            newSampleData.put("race", "Black");
            newSampleData.put("sex", "Male");
            newSampleData.put("capital_gain", 0);
            newSampleData.put("capital_loss", 0);
            newSampleData.put("hours_per_week", 40);
            newSampleData.put("native_country", "United-States");

            Instance newRawWekaInstance = featureExtractor.createWekaInstanceFromMap(newSampleData);

            Instances newInstancesForPreprocess = new Instances(featureExtractor.getHeaderInformation(), 1);
            newInstancesForPreprocess.add(newRawWekaInstance);

            LOGGER.info("Pré-processando nova amostra para predição...");
            Instances preprocessedNewSample = featureExtractor.preprocessNewSample(newInstancesForPreprocess);

            PredictionService predictionService = new PredictionService(model, featureExtractor.getHeaderInformation());

            String predictedClass = predictionService.predict(preprocessedNewSample.firstInstance());
            LOGGER.info("Predição para a nova amostra: " + predictedClass);
            */


        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocorreu um erro crítico na aplicação de ML: " + e.getMessage(), e);
            System.err.println("\n--- ERRO FATAL NA APLICAÇÃO ---");
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("-------------------------------\n");
        }

        LOGGER.info("Aplicação de Machine Learning Pura Java finalizada.");
    }

    private static boolean hasConsoleHandler(Logger logger) {
        for (java.util.logging.Handler handler : logger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                return true;
            }
        }
        return false;
    }
}