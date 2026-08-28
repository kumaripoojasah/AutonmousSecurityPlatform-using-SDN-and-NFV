package com.example.demo.service;


import com.example.demo.dto.attack.AttackResponse;
import com.example.demo.entity.Attack;
import com.example.demo.repository.AttackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttackService {

    private final AttackRepository attackRepository;

    public List<AttackResponse> getRecentAttacks() {

        return attackRepository
                .findTop10ByOrderByDetectedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AttackResponse getAttackById(Long id) {

        Attack attack = attackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attack not found: " + id)
                );

        return toResponse(attack);
    }

    private AttackResponse toResponse(Attack attack) {

        return AttackResponse.builder()
                .id(attack.getId())
                .networkId(attack.getNetwork().getId())
                .attackType(attack.getAttackType().name())
                .sourceIp(attack.getSourceIp())
                .targetIp(attack.getTargetIp())
                .severity(attack.getSeverity().name())
                .confidence(attack.getConfidence())
                .detectedAt(attack.getDetectedAt())
                .status(attack.getStatus().name())
                .description(attack.getDescription())
                .build();
    }
}