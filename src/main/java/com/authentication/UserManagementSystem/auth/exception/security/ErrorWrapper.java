package com.authentication.UserManagementSystem.auth.exception.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
@Getter @Setter
public class ErrorWrapper {
    private List<RestError> error;
    public ErrorWrapper(RestError restError){
        this.error = Collections.singletonList(restError);
    }
}
