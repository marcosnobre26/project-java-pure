package com.machinelearning.purejava.ml.features;

import com.machinelearning.purejava.ml.config.FeatureConfig;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FeatureExtractor {

    private static final Logger LOGGER = Logger.getLogger(FeatureExtractor.class.getName());
    private FeatureConfig featureConfig;
    private Instances headerInformation; // Armazena o cabeçalho final após o pré-processamento de treinamento

    // Filtros Weka pré-configurados e treinados
    private NominalToBinary nominalToBinaryFilter;
    private Standardize standardizeFilter;
    private ReplaceMissingValues replaceMissingValuesFilter;


    public FeatureExtractor(FeatureConfig featureConfig) throws Exception {
        this.featureConfig = featureConfig;
        LOGGER.info("Inicializando FeatureExtractor com configurações: " + featureConfig);

        // Inicializa os filtros Weka
        nominalToBinaryFilter = new NominalToBinary();
        standardizeFilter = new Standardize();
        replaceMissingValuesFilter = new ReplaceMissingValues();
    }

    /**
     * Pré-processa um conjunto de dados brutos (treinamento ou teste).
     *
     * @param rawData O conjunto de dados brutos.
     * @return O conjunto de dados pré-processado.
     * @throws Exception Se houver um erro durante o pré-processamento.
     */
    public Instances preprocess(Instances rawData) throws Exception {
        LOGGER.info("Iniciando pré-processamento de dados...");
        Instances processedData = new Instances(rawData); // Crie uma cópia para não modificar o original

        // 1. Tratar valores ausentes (Missing Values)
        if (featureConfig.isReplaceMissingValues()) { // Usando a propriedade do FeatureConfig
            LOGGER.info("Aplicando filtro ReplaceMissingValues...");
            replaceMissingValuesFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, replaceMissingValuesFilter);
            LOGGER.info("Filtro ReplaceMissingValues aplicado.");
        }


        // 2. One-Hot Encoding para Categorical Features
        // AQUI ESTÁ A CORREÇÃO: featureConfig.getCategoricalEncoding() AGORA RETORNA A STRING DIRETA
        String categoricalEncodingStrategy = featureConfig.getCategoricalEncoding();
        if (featureConfig.isOneHotEncodeCategorical() && "ONE_HOT_ENCODING".equalsIgnoreCase(categoricalEncodingStrategy)) {
            LOGGER.info("Aplicando One-Hot Encoding para atributos categóricos...");
            nominalToBinaryFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, nominalToBinaryFilter);
            LOGGER.info("One-Hot Encoding aplicado.");
        } else {
            // Este warning será exibido se oneHotEncodeCategorical for false ou a estratégia não for ONE_HOT_ENCODING
            LOGGER.warning("One-Hot Encoding não será aplicado. Configuração: oneHotEncodeCategorical=" +
                           featureConfig.isOneHotEncodeCategorical() + ", strategy=" + categoricalEncodingStrategy);
        }

        // 3. Normalização/Padronização de Features Numéricas (opcional, como exemplo)
        // Exemplo com Standardize (Z-score normalization)
        if (featureConfig.isStandardizeNumeric() && !featureConfig.getNumericalFeatures().isEmpty()) { // Usando a propriedade do FeatureConfig
            LOGGER.info("Aplicando padronização (Standardize) para atributos numéricos...");
            standardizeFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, standardizeFilter);
            LOGGER.info("Padronização aplicada.");
        }

        // CRÍTICO: Armazena o headerInformation dos dados pré-processados
        // Isso é essencial para pré-processar novas amostras e para o PredictionService
        this.headerInformation = new Instances(processedData, 0); // Copia apenas a estrutura dos atributos
        LOGGER.info("Pré-processamento concluído. Header information armazenado.");
        return processedData;
    }

    /**
     * Pré-processa uma nova amostra (uma única instância) usando os filtros treinados.
     * Esta é a versão usada para predição em tempo real.
     * @param newSampleInstances O conjunto de dados contendo a nova amostra (geralmente uma única instância).
     * @return O conjunto de dados com a amostra pré-processada.
     * @throws Exception Se houver um erro durante o pré-processamento.
     */
    public Instances preprocessNewSample(Instances newSampleInstances) throws Exception {
        LOGGER.info("Iniciando pré-processamento de nova amostra...");
        Instances preprocessedSample = new Instances(newSampleInstances); // Cópia

        // Aplique os mesmos filtros na mesma ordem que foram aplicados aos dados de treino
        // Os filtros já foram treinados no método `preprocess` quando você passou os dados de treino
        // e, portanto, retêm o estado necessário para transformar novas instâncias.

        // 1. Tratar valores ausentes
        if (featureConfig.isReplaceMissingValues()) {
            replaceMissingValuesFilter.setInputFormat(preprocessedSample);
            preprocessedSample = Filter.useFilter(preprocessedSample, replaceMissingValuesFilter);
        }

        // 2. One-Hot Encoding (se aplicado no treinamento)
        String categoricalEncodingStrategy = featureConfig.getCategoricalEncoding();
        if (featureConfig.isOneHotEncodeCategorical() && "ONE_HOT_ENCODING".equalsIgnoreCase(categoricalEncodingStrategy)) {
            nominalToBinaryFilter.setInputFormat(preprocessedSample);
            preprocessedSample = Filter.useFilter(preprocessedSample, nominalToBinaryFilter);
        }

        // 3. Normalização/Padronização (se aplicado no treinamento)
        if (featureConfig.isStandardizeNumeric() && !featureConfig.getNumericalFeatures().isEmpty()) {
            standardizeFilter.setInputFormat(preprocessedSample);
            preprocessedSample = Filter.useFilter(preprocessedSample, standardizeFilter);
        }

        LOGGER.info("Pré-processamento da nova amostra concluído.");
        return preprocessedSample;
    }

    /**
     * Cria uma única instância Weka a partir de um mapa de dados brutos.
     * Esta instância ainda precisará passar pelos filtros do FeatureExtractor.
     *
     * @param dataMap Um mapa onde as chaves são nomes de atributos e os valores são os dados brutos.
     * @return Uma instância Weka (singular).
     */
    public Instance createWekaInstanceFromMap(Map<String, Object> dataMap) {
        if (this.headerInformation == null) {
            throw new IllegalStateException("Header information is not initialized. Preprocess training data first.");
        }

        Instance instance = new DenseInstance(this.headerInformation.numAttributes());
        instance.setDataset(this.headerInformation); // VINCULA AO CABEÇALHO!

        for (int i = 0; i < this.headerInformation.numAttributes(); i++) {
            Attribute attr = this.headerInformation.attribute(i);
            String attrName = attr.name();
            Object value = dataMap.get(attrName);

            if (value != null) {
                if (attr.isNumeric()) {
                    try {
                        instance.setValue(attr, Double.parseDouble(value.toString()));
                    } catch (NumberFormatException e) {
                        instance.setMissing(attr);
                        LOGGER.warning("Valor não numérico para atributo numérico '" + attrName + "': " + value);
                    }
                } else if (attr.isNominal() || attr.isString()) {
                    if (attr.isNominal() && attr.indexOfValue(value.toString()) == -1) {
                        instance.setMissing(attr);
                        LOGGER.warning("Valor nominal '" + value + "' para atributo '" + attrName + "' não encontrado nos valores esperados. Marcando como ausente.");
                    } else {
                        instance.setValue(attr, value.toString());
                    }
                } else {
                    instance.setMissing(attr);
                }
            } else {
                instance.setMissing(attr);
            }
        }
        return instance;
    }

    /**
     * Retorna o cabeçalho do dataset após o pré-processamento.
     * Essencial para o PredictionService e para a consistência dos dados.
     * @return As informações de cabeçalho (estrutura dos atributos).
     */
    public Instances getHeaderInformation() {
        return headerInformation;
    }
}