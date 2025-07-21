// Local: src/main/java/com/machinelearning/purejava/main/dto/PredictionRequest.java
package com.machinelearning.purejava.main.dto;

import java.util.Map;
import java.util.HashMap;

// Esta classe representa os dados que um usuário enviará para a API.
// Usamos um Map para flexibilidade, assim podemos enviar quaisquer características.
public class PredictionRequest {

    private Map<String, Object> features = new HashMap<>();

    // Getters e Setters
    public Map<String, Object> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Object> features) {
        this.features = features;
    }
}