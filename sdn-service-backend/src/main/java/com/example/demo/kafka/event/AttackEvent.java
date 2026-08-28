package com.example.demo.kafka.event;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttackEvent {

    private Long networkId;

    private String attackType;

    private String sourceIp;

    private String targetIp;

    private String severity;

    private Double confidence;

    private LocalDateTime detectedAt;

    private String description;
}