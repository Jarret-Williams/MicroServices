package com.example.Microservices.exceptions.BAD_REQ;

public class NameTakenRequestException extends RuntimeException {
    public NameTakenRequestException(String message) {
        super(message);
    }
    public NameTakenRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
