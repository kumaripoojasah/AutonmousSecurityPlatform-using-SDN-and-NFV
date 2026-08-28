package com.example.demo.service;



import com.example.demo.dto.traffic.ModelInputFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PolicyEngineService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyEngineService.class);

    // Dynamic Policy Thresholds
    private static final double SYN_FLOOD_THRESHOLD = 5.0; // SYN/ACK ratio
    private static final double HIGH_PACKET_RATE_THRESHOLD = 500.0; // packets/sec
    private static final int CRITICAL_SYN_COUNT = 30;

    public String determineMitigationAction(String modelPrediction, ModelInputFeatures features) {

        // If ML model flags traffic as normal, no action required
        if ("NORMAL".equalsIgnoreCase(modelPrediction) || "TRAFFIC_NORMAL".equalsIgnoreCase(modelPrediction)) {
            return "NO_ACTION";
        }

        logger.warn("Threat detected by ML model: {} for device {}", modelPrediction, features.getDeviceId());

        // Rule-Based Severity Evaluation
        if (features.getSynToAckRatio() > SYN_FLOOD_THRESHOLD && features.getTcpFlagSynCount() > CRITICAL_SYN_COUNT) {
            return "BLOCK_IP"; // Critical SYN Flood attack -> Instantly block IP
        }

        if (features.getForwardPacketRate() > HIGH_PACKET_RATE_THRESHOLD) {
            return "RATE_LIMIT"; // High-volume packet spike -> Throttle bandwidth
        }

        // Default mitigation action for unclassified ML threats
        return "ALERT_AND_MONITOR";
    }
}