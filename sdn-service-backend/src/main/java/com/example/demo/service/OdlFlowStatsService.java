package com.example.demo.service;

import com.example.demo.config.KafkaConfig; // Import your KafkaConfig constants
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
public class OdlFlowStatsService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestClient restClient;

    @Value("${opendaylight.url}")
    private String odlUrl;

    public OdlFlowStatsService(KafkaTemplate<String, String> kafkaTemplate,
                               @Value("${opendaylight.username}") String username,
                               @Value("${opendaylight.password}") String password) {
        this.kafkaTemplate = kafkaTemplate;

        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        this.restClient = RestClient.builder()
                .defaultHeader("Authorization", authHeader)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Scheduled(fixedRate = 5000)
    public void fetchAndPublishFlowStats() {
        try {
            String response = restClient.get()
                    .uri(odlUrl)
                    .retrieve()
                    .body(String.class);

            if (response != null) {
                // Send raw OpenDaylight stats directly to the traffic-events topic
                kafkaTemplate.send(KafkaConfig.TRAFFIC_TOPIC, response);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch statistics from ODL: " + e.getMessage());
        }
    }
}