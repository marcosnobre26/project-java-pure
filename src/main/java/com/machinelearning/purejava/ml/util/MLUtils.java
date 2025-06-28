package com.machinelearning.purejava.ml.util;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MLUtils {

    private static final Logger LOGGER = Logger.getLogger(MLUtils.class.getName());

    /**
     * Converte uma String para um Double de forma segura, retornando null ou um valor padrão se houver erro.
     * @param str A string a ser convertida.
     * @return O valor Double, ou null se a conversão falhar.
     */
    public static Double safeParseDouble(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("Não foi possível converter '" + str + "' para Double.");
            return null;
        }
    }

    /**
     * Converte uma String para um Integer de forma segura.
     * @param str A string a ser convertida.
     * @return O valor Integer, ou null se a conversão falhar.
     */
    public static Integer safeParseInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("Não foi possível converter '" + str + "' para Integer.");
            return null;
        }
    }

    /**
     * Imprime um resumo básico de um dataset Weka.
     * @param dataset O dataset Weka.
     */
    public static void printDatasetSummary(Instances dataset) {
        if (dataset == null) {
            LOGGER.info("Dataset é nulo.");
            return;
        }
        LOGGER.info("--- Resumo do Dataset ---");
        LOGGER.info("Nome do Dataset: " + dataset.relationName());
        LOGGER.info("Número de Instâncias (linhas): " + dataset.numInstances());
        LOGGER.info("Número de Atributos (colunas): " + dataset.numAttributes());
        if (dataset.classIndex() != -1) {
            LOGGER.info("Atributo Alvo: " + dataset.classAttribute().name());
        }
        LOGGER.info("-------------------------");
    }

    /**
     * Converte um Map<String, Object> para uma instância Weka.Instance, usando as informações de cabeçalho.
     * Este é um helper genérico, útil em DataLoader ou PredictionService.
     *
     * @param dataMap O mapa com os dados da linha.
     * @param headerInfo A estrutura de atributos do dataset (header).
     * @return Uma instância Weka.Instance populada.
     */
    public static Instance createWekaInstanceFromMap(Map<String, Object> dataMap, Instances headerInfo) {
        Instance instance = new DenseInstance(headerInfo.numAttributes());
        instance.setDataset(headerInfo); // Vincula ao cabeçalho

        for (int i = 0; i < headerInfo.numAttributes(); i++) {
            Attribute attr = headerInfo.attribute(i);
            String attrName = attr.name();
            Object value = dataMap.get(attrName);

            if (value != null) {
                if (attr.isNumeric()) {
                    Double numValue = safeParseDouble(value.toString());
                    if (numValue != null) {
                        instance.setValue(attr, numValue);
                    } else {
                        instance.setMissing(attr); // Não conseguiu converter para numérico
                    }
                } else if (attr.isString() || attr.isNominal()) {
                    // Para atributos nominais, o valor deve existir na lista de valores do atributo.
                    // Weka lida com valores desconhecidos.
                    instance.setValue(attr, value.toString());
                } else {
                    instance.setMissing(attr); // Tipo de atributo não suportado
                }
            } else {
                instance.setMissing(attr); // Valor ausente no mapa
            }
        }
        return instance;
    }

    // Exemplo de helper para fechar recursos de banco de dados
    public static void closeJdbcResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.severe("Erro ao fechar recursos JDBC: " + e.getMessage());
        }
    }

    // Outros utilitários poderiam incluir:
    // - Métodos para calcular métricas simples (se não for usar a classe Evaluation do Weka)
    // - Funções para gerar IDs únicos.
    // - Métodos para leitura/escrita de arquivos de texto simples que não são dados brutos.
}
