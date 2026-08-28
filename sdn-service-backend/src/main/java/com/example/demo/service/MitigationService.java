package com.example.demo.service;

import com.example.demo.dto.mitigation.MitigationActionResponse;
import com.example.demo.dto.traffic.ModelInputFeatures;
import com.example.demo.entity.MitigationAction;
import com.example.demo.kafka.event.TrafficEvent;
import com.example.demo.repository.MitigationActionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MitigationService {

    private static final Logger logger = LoggerFactory.getLogger(MitigationService.class);
    private final MitigationActionRepository mitigationActionRepository;

    // --- Query Methods (Your Pre-Defined Logic) ---

    public List<MitigationActionResponse> getRecentActions() {
        return mitigationActionRepository
                .findTop10ByOrderByExecutedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MitigationActionResponse getActionById(Long id) {
        MitigationAction action = mitigationActionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mitigation action not found: " + id));

        return toResponse(action);
    }

    // --- Execution Method (Added for Kafka Enforcement) ---

    public void executeMitigation(String action, TrafficEvent rawEvent, ModelInputFeatures features) {
        switch (action) {
            case "BLOCK_IP":
                logger.warn(">>> ENFORCING SDN RULE: Blocking IP {} on Device {}",
                        rawEvent.getSourceIp(), features.getDeviceId());
                // TODO: Call your sdnControllerClient or save to mitigationActionRepository here
                break;

            case "RATE_LIMIT":
                logger.warn(">>> ENFORCING SDN RULE: Rate-limiting traffic for Device {}",
                        features.getDeviceId());
                // TODO: Call your sdnControllerClient or save to mitigationActionRepository here
                break;

            case "ALERT_AND_MONITOR":
                logger.info(">>> LOGGING ALERT: Suspicious activity monitored for Device {}",
                        features.getDeviceId());
                break;

            case "NO_ACTION":
            default:
                logger.info(">>> NO MITIGATION REQUIRED: Network operating normally.");
                break;
        }
    }

    // --- Helper Converter ---

    private MitigationActionResponse toResponse(MitigationAction action) {
        return MitigationActionResponse.builder()
                .id(action.getId())
                .attackId(action.getAttack() != null ? action.getAttack().getId() : null)
                .actionType(action.getActionType().name())
                .actionTarget(action.getActionTarget())
                .actionValue(action.getActionValue())
                .status(action.getStatus().name())
                .executedAt(action.getExecutedAt())
                .executionDetails(action.getExecutionDetails())
                .build();
    }
}
