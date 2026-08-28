package com.example.demo.dto.mitigation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MitigationActionResponse {

    private Long id;

    private Long attackId;

    private String actionType;

    private String actionTarget;

    private String actionValue;

    private String status;

    private LocalDateTime executedAt;

    private String executionDetails;
}