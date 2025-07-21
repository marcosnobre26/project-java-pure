package com.machinelearning.purejava.ml.features;

import com.machinelearning.purejava.ml.config.FeatureConfig;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Classe responsável por pré-processar os dados.
 * Implementa Serializable para que seu estado (filtros treinados e cabeçalhos)
 * possa ser salvo em um arquivo e carregado posteriormente pela API.
 */
public class FeatureExtractor implements Serializable {

    // ID de versão para garantir compatibilidade durante a serialização.
    private static final long serialVersionUID = 2L;

    private static final Logger LOGGER = Logger.getLogger(FeatureExtractor.class.getName());

    // --- CORREÇÃO AQUI ---
    // Marcamos o featureConfig como 'transient' para que ele seja ignorado
    // durante o processo de salvar (serialização), resolvendo o erro.
    private final transient FeatureConfig featureConfig;
    
    // Configurações e filtros que serão "treinados" e salvos.
    private Instances rawDataHeader; // GUARDA A ESTRUTURA DOS DADOS BRUTOS (ESSENCIAL!)
    private NominalToBinary nominalToBinaryFilter;
    private Standardize standardizeFilter;
    private ReplaceMissingValues replaceMissingValuesFilter;

    public FeatureExtractor(FeatureConfig featureConfig) {
        this.featureConfig = featureConfig;
        LOGGER.info("Inicializando FeatureExtractor...");
    }

    /**
     * Pré-processa os dados de treinamento. Este método "treina" os filtros
     * e armazena a estrutura dos dados.
     *
     * @param rawData O conjunto de dados de treinamento.
     * @return O conjunto de dados pré-processado.
     */
    public Instances preprocess(Instances rawData) throws Exception {
        LOGGER.info("Iniciando pré-processamento de dados de treinamento...");
        // CRÍTICO: Armazena a estrutura dos dados BRUTOS para poder criar novas instâncias depois.
        this.rawDataHeader = new Instances(rawData, 0);

        Instances processedData = new Instances(rawData);

        // 1. Tratar valores ausentes
        if (featureConfig.isReplaceMissingValues()) {
            LOGGER.info("Treinando e aplicando filtro ReplaceMissingValues...");
            replaceMissingValuesFilter = new ReplaceMissingValues();
            replaceMissingValuesFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, replaceMissingValuesFilter);
        }

        // 2. One-Hot Encoding para atributos categóricos
        if (featureConfig.isOneHotEncodeCategorical()) {
            LOGGER.info("Treinando e aplicando One-Hot Encoding...");
            nominalToBinaryFilter = new NominalToBinary();
            nominalToBinaryFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, nominalToBinaryFilter);
        }

        // 3. Padronização de atributos numéricos
        if (featureConfig.isStandardizeNumeric()) {
            LOGGER.info("Treinando e aplicando padronização (Standardize)...");
            standardizeFilter = new Standardize();
            standardizeFilter.setInputFormat(processedData);
            processedData = Filter.useFilter(processedData, standardizeFilter);
        }

        LOGGER.info("Pré-processamento de treinamento concluído.");
        return processedData;
    }

    /**
     * Pré-processa UMA NOVA amostra usando os filtros JÁ TREINADOS.
     *
     * @param newRawInstance A nova instância de dados brutos a ser processada.
     * @return A instância após passar por todos os filtros.
     */
    public Instances preprocessNewSample(Instance newRawInstance) throws Exception {
        // Para aplicar filtros, precisamos de um objeto Instances, mesmo que para uma única linha.
        Instances newDataSet = new Instances(this.rawDataHeader, 1);
        newDataSet.add(newRawInstance);

        // Aplica os filtros na mesma ordem, mas agora sem treiná-los novamente.
        if (replaceMissingValuesFilter != null) {
            newDataSet = Filter.useFilter(newDataSet, replaceMissingValuesFilter);
        }
        if (nominalToBinaryFilter != null) {
            newDataSet = Filter.useFilter(newDataSet, nominalToBinaryFilter);
        }
        if (standardizeFilter != null) {
            newDataSet = Filter.useFilter(newDataSet, standardizeFilter);
        }

        return newDataSet;
    }

    /**
     * Cria uma instância Weka a partir de um mapa de dados (ex: vindo de um JSON).
     * Usa o cabeçalho dos dados brutos que foi salvo durante o treinamento.
     *
     * @param dataMap Mapa com os nomes e valores dos atributos.
     * @return Uma instância Weka pronta para ser pré-processada.
     */
    public Instance createWekaInstanceFromMap(Map<String, Object> dataMap) {
        if (this.rawDataHeader == null) {
            throw new IllegalStateException("O extrator não foi treinado. Execute o preprocessamento com dados de treino primeiro.");
        }

        Instance instance = new DenseInstance(this.rawDataHeader.numAttributes());
        instance.setDataset(this.rawDataHeader); // VINCULA AO CABEÇALHO DOS DADOS BRUTOS!

        for (int i = 0; i < this.rawDataHeader.numAttributes(); i++) {
            Attribute attr = this.rawDataHeader.attribute(i);
            Object value = dataMap.get(attr.name());

            if (value == null) {
                instance.setMissing(attr);
                continue;
            }

            try {
                if (attr.isNumeric()) {
                    instance.setValue(attr, Double.parseDouble(value.toString()));
                } else if (attr.isNominal()) {
                    // Para atributos nominais, o valor deve existir na lista de valores possíveis.
                    // Se não existir, marcamos como ausente para o filtro ReplaceMissingValues tratar.
                    if (attr.indexOfValue(value.toString()) != -1) {
                        instance.setValue(attr, value.toString());
                    } else {
                        instance.setMissing(attr);
                        LOGGER.warning("Valor '" + value + "' não encontrado para o atributo nominal '" + attr.name() + "'. Marcando como ausente.");
                    }
                } else {
                    instance.setValue(attr, value.toString());
                }
            } catch (Exception e) {
                LOGGER.warning("Erro ao definir valor para o atributo '" + attr.name() + "'. Valor: " + value + ". Marcando como ausente. Erro: " + e.getMessage());
                instance.setMissing(attr);
            }
        }
        return instance;
    }
}
