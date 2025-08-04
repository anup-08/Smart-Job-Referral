package com.smartJob.exception;

import org.apache.tomcat.util.json.TokenMgrError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> map = new HashMap<>();

        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrorList = result.getFieldErrors();

        for(FieldError error : fieldErrorList){
            map.put(error.getField() , error.getDefaultMessage());
        }
        return new ResponseEntity<>(map , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AlreadyRegisterException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyRegisterException(AlreadyRegisterException exception){
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.value()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(NotFound ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("API endpoint not found: " + ex.getRequestURL(), HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex){
        return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        return new ResponseEntity<>(
                new ErrorResponse( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleTokenMgrError(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ErrorResponse( ex.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }


}
