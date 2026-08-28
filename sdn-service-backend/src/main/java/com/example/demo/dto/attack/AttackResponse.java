package com.example.demo.dto.attack;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttackResponse {

    private Long id;

    private Long networkId;

    private String attackType;

    private String sourceIp;

    private String targetIp;

    private String severity;

    private Double confidence;

    private LocalDateTime detectedAt;

    private String status;

    private String description;
}