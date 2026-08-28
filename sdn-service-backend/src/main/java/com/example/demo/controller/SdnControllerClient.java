package com.example.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SdnControllerClient {

    private final WebClient.Builder webClientBuilder;

    public boolean blockIpAddress(String targetIp) {
        try {
            // Adjust the URL to match your SDN Controller REST API endpoint
            Map<String, Object> requestBody = Map.of(
                    "targetIp", targetIp,
                    "action", "DROP",
                    "priority", 100
            );

            String response = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8181/restconf/config/sdn/flow-rule")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking call to wait for ACK

            log.info("Successfully pushed block rule to SDN Controller for IP: {}", targetIp);
            return true;
        } catch (Exception e) {
            log.error("Failed to push rule to SDN Controller for IP: {}", targetIp, e);
            return false;
        }
    }
}