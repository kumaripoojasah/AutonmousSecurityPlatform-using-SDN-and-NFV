package com.example.demo.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MitigationEvent {

    private Long attackId;

    private String actionType;

    private String actionTarget;

    private String actionValue;

    private String status;

    private LocalDateTime executedAt;

    private String executionDetails;
}