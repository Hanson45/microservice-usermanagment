package com.authentication.UserManagementSystem.auth.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter @Getter
@Builder
public class RegisterResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String token;
    private Boolean isActive;
}
