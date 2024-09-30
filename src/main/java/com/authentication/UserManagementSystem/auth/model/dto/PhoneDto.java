package com.authentication.UserManagementSystem.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {
    private Long number;
    private Integer cityCode;
    private String countryCode;
}
