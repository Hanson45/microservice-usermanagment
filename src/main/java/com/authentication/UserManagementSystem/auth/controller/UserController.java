package com.authentication.UserManagementSystem.auth.controller;


import com.authentication.UserManagementSystem.auth.model.dto.LoginResponse;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterResponse;
import com.authentication.UserManagementSystem.auth.model.dto.RegisterUserDto;
import com.authentication.UserManagementSystem.auth.model.entity.User;
import com.authentication.UserManagementSystem.auth.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@AllArgsConstructor
public class UserController {

    private final AuthenticationService service;

    @PostMapping("/sign-up")
    public RegisterResponse createUser(@RequestBody RegisterUserDto newUser){
        return service.register(newUser);
    }

    @GetMapping("/login")
    public LoginResponse  login(Authentication auth){
         return service.login(auth);
    }

}
