package com.authentication.UserManagementSystem.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        ErrorDetail errorResponse = new ErrorDetail(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(List.of(errorResponse));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex){
        ErrorDetail errorResponse = new ErrorDetail(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(List.of(errorResponse));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),ex.getMessage());
        ErrorResponse response = new ErrorResponse(List.of(errorDetail));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),"Resource not found");
        ErrorResponse response = new ErrorResponse(List.of(errorDetail));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(NoHandlerFoundException ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal server error");
        ErrorResponse response = new ErrorResponse(List.of(errorDetail));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
