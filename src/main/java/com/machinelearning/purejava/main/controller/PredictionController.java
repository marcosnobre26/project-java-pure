// Local: src/main/java/com/machinelearning/purejava/main/controller/PredictionController.java
package com.machinelearning.purejava.main.controller;

import com.machinelearning.purejava.main.dto.PredictionRequest;
import com.machinelearning.purejava.main.dto.PredictionResponse;
import com.machinelearning.purejava.main.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PredictionController {

    private final PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> getPrediction(@RequestBody PredictionRequest request) {
        try {
            // O serviço agora retorna o objeto de resposta completo
            PredictionResponse response = predictionService.predict(request.getFeatures());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Em caso de erro, usamos o construtor de erro do PredictionResponse
            PredictionResponse errorResponse = new PredictionResponse("Erro ao processar a predição: " + e.getMessage());
            // Retorna um status 500 (Internal Server Error) com a mensagem de erro no corpo
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}