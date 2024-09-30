package com.authentication.UserManagementSystem.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RegisterUserDto {

    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    private List<PhoneDto> phones;

}





