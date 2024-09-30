package com.authentication.UserManagementSystem.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class ErrorResponse {
    private List<ErrorDetail> error;
}
