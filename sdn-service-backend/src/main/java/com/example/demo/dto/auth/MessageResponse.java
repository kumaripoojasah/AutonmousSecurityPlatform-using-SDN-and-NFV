package com.example.demo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// Generic response for endpoints that don't need to return a user object,
// e.g. forgot-password and reset-password confirmations.
@Getter
@Builder
@AllArgsConstructor
public class MessageResponse {

    private String message;
}