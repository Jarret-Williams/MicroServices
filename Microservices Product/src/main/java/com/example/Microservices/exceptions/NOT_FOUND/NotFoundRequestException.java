package com.example.Microservices.exceptions.NOT_FOUND;

public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException(String message) {
        super(message);
    }
    public NotFoundRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
