package com.example.demo.service;

import com.example.demo.dto.traffic.ModelInputFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DecisionModelService {

    private static final Logger logger = LoggerFactory.getLogger(DecisionModelService.class);
    private final RestTemplate restTemplate;

    private static final String PYTHON_ENGINE_URL = "http://localhost:5000/predict";

    public DecisionModelService() {
        this.restTemplate = new RestTemplate();
    }

    public String predict(ModelInputFeatures features) {
        try {
            // Option 1: Send feature vector array (17 elements)
            double[] featureVector = features.toFeatureVector();

            Map<String, Object> payload = new HashMap<>();
            payload.put("features", Arrays.stream(featureVector).boxed().toArray());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // POST to Python decision engine
            Map<?, ?> response = restTemplate.postForObject(PYTHON_ENGINE_URL, entity, Map.class);

            if (response != null && response.containsKey("prediction")) {
                return response.get("prediction").toString();
            }
            return "NORMAL";

        } catch (Exception e) {
            logger.error("Failed to connect to Python Decision Engine: {}", e.getMessage());
            return "ERROR_FALLBACK_NORMAL";
        }
    }
}