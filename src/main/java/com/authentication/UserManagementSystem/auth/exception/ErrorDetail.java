package com.authentication.UserManagementSystem.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetail {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
}
