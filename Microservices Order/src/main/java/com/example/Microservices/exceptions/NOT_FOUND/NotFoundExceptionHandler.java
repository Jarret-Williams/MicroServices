package com.example.Microservices.exceptions.NOT_FOUND;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(value = {NotFoundRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(NotFoundRequestException e){
        //1. Create payload containing exception details
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        NotFoundException apiException = new NotFoundException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. Return response entity
        return new ResponseEntity<>(apiException, badRequest);
    }
}
