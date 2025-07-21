// Local: src/main/java/com/machinelearning/purejava/main/dto/PredictionResponse.java
package com.machinelearning.purejava.main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Representa a resposta enriquecida da API de predição.
 * Usamos @JsonInclude(JsonInclude.Include.NON_NULL) para não mostrar campos nulos no JSON final.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PredictionResponse {

    private final String predictionId;
    private final Instant timestamp;
    private String status; // Ex: "SUCCESS", "ERROR"
    private String predictedClass;
    private Map<String, Double> probabilities;
    private String errorMessage;

    // Construtor para uma resposta de sucesso
    public PredictionResponse(String predictedClass, Map<String, Double> probabilities) {
        this.predictionId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.status = "SUCCESS";
        this.predictedClass = predictedClass;
        this.probabilities = probabilities;
    }

    // Construtor para uma resposta de erro
    public PredictionResponse(String errorMessage) {
        this.predictionId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.status = "ERROR";
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getPredictionId() {
        return predictionId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getPredictedClass() {
        return predictedClass;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
